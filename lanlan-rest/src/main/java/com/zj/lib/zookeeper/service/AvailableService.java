package com.zj.lib.zookeeper.service;

import com.zj.bo.RouteInfo;
import com.zj.constant.ErrorConstant;
import com.zj.exception.BusinessException;
import com.zj.lib.cache.ZkServerListCache;
import com.zj.lib.zookeeper.kit.NetAddressIsReachable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class AvailableService {

        @Autowired
        private ZkServerListCache zkServerListCache;

        /**
         * check ip and port
         * @param routeInfo
         */
        public void checkServerAvailable(RouteInfo routeInfo){
            boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getServerPort(), 1000);
            if (!reachable) {
                log.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getServerPort());

                // rebuild cache
                zkServerListCache.rebuildCacheList();

                throw new BusinessException(ErrorConstant.SERVER_NOT_AVAILABLE) ;
            }

        }

}
