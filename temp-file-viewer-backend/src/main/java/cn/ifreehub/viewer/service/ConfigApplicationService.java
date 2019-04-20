package cn.ifreehub.viewer.service;

import com.google.common.collect.Maps;

import org.springframework.stereotype.Service;

import cn.ifreehub.viewer.domain.FileIndexReference;
import cn.ifreehub.viewer.domain.UserConfig;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Quding Ding
 * @since 2018/6/20
 */
@Service
public class ConfigApplicationService {

  @Resource
  private FileIndexReferenceService fileIndexReferenceService;

  /**
   * 拿到全局配置
   * @return 配置
   */
  public UserConfig getUserConfig() {
    return fileIndexReferenceService.getUserConfig();
  }

  /**
   * 得到已上传的文件
   * @return 已上传文件集合
   */
  public Map<String, FileIndexReference> getFiles() {
    return Optional.ofNullable(fileIndexReferenceService.getUserConfig().getFiles())
        .orElse(Maps.newHashMapWithExpectedSize(2));
  }

}
