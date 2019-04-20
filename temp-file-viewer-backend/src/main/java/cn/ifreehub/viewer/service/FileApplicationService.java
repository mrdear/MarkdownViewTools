package cn.ifreehub.viewer.service;

import com.google.common.collect.Lists;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.ifreehub.viewer.domain.FileIndexReference;
import cn.ifreehub.viewer.domain.UserConfig;
import cn.ifreehub.viewer.exception.ServiceException;
import cn.ifreehub.viewer.repo.TinifyPngRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 领域对应的服务
 *
 * @author Quding Ding
 * @since 2018/6/17
 */
@Service
public class FileApplicationService {
  private static Logger logger = LoggerFactory.getLogger(FileApplicationService.class);

  @Resource
  private FileIndexReferenceService fileIndexReferenceService;
  @Resource
  private TinifyPngRepo tinifyPngRepo;
  /**
   * 添加一个文件
   *
   * @param file 要添加的文件信息
   */
  public void addFileIndex(FileIndexReference reference, MultipartFile file) {
    // 保存config
    if (fileIndexReferenceService.addFileIndexConfig(reference)) {
      // 保存文件
      try {
        file.transferTo(new File(reference.getFileAbsolutePath()));
        tinifyPngRepo.asyncShrinkPicture(reference);
      } catch (IOException e) {
        logger.error("save file fail, file is {}", reference, e);
        throw new ServiceException(e);
      }
    }
  }

  /**
   * 删除文件
   *
   * @param reference 文件索引
   */
  public void removeFileIndex(FileIndexReference reference) {
    // 删除文件成功,清理磁盘
    if (fileIndexReferenceService.removeFileIndex(reference)) {
      try {
        FileUtils.forceDelete(new File(reference.getFileAbsolutePath()));
        return;
      } catch (IOException e) {
        logger.error("delete file fail, file is {}", reference.getFileAbsolutePath(), e);
        throw new ServiceException(e);
      }
    }

    throw new ServiceException("delete file fail");
  }

  /**
   * 查询索引中全部的文件
   *
   * @return 结果
   */
  public List<FileIndexReference> queryAllFile() {
    UserConfig userConfig = fileIndexReferenceService.getUserConfig();
    Map<String, FileIndexReference> files = userConfig.getFiles();

    ArrayList<FileIndexReference> references = Lists.newArrayList(files.values());

    return references.stream()
        // 过滤掉过期文件
        .filter(x -> {
          if (!x.valid()) {
            fileIndexReferenceService.removeFileIndex(x);
            return false;
          }
          return true;
        })
        .sorted()
        .collect(Collectors.toList());
  }

  /**
   * 更新文件信息
   * @param reference 文件信息
   * @return true成功
   */
  public boolean updateFileReference(FileIndexReference reference) {
    return fileIndexReferenceService.addFileIndexConfig(reference);
  }
}
