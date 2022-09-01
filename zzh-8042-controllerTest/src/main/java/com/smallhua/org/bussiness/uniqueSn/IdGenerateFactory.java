package com.smallhua.org.bussiness.uniqueSn;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈id生成工厂〉
 *
 * @author ZZH
 * @create 2022/8/31
 * @since 1.0.0
 */
@Component
public class IdGenerateFactory {

    @Autowired
    public NfmNodeInfo nfmNodeInfo;

    private Map<BusinessTypeEnum, NfmIdGenerate> caches = Maps.newHashMap();

    public NfmIdGenerate getInstance(BusinessTypeEnum businessTypeEnum){
        if (caches.containsKey(businessTypeEnum)) return caches.get(businessTypeEnum);

        NfmIdGenerate nfmIdGenerate = new NfmIdGenerate(nfmNodeInfo, businessTypeEnum.getDesc());
        caches.put(businessTypeEnum, nfmIdGenerate);

        return nfmIdGenerate;
    }

}