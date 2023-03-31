package org.hj.yygh.dict.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hj
 * @data 2023/3/28 19:10
 */
@Service
@FeignClient("service-dict")
public interface DictFeignClient {

    @GetMapping("/admin/dict/dictionary/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,@PathVariable("value") String value);
    @GetMapping("/admin/dict/dictionary/getName/{value}")
    public String getName(@PathVariable("value") String value);


}
