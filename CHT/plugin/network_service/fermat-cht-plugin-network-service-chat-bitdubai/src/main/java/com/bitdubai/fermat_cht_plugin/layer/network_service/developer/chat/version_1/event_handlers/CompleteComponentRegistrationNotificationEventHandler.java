package com.bitdubai.fermat_cht_plugin.layer.network_service.developer.chat.version_1.event_handlers;

import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.event_handlers.AbstractCompleteComponentRegistrationNotificationEventHandler;

/**
 * Created by Gabriel Araujo on 05/01/16.
 */
public class CompleteComponentRegistrationNotificationEventHandler extends AbstractCompleteComponentRegistrationNotificationEventHandler {
    /**
     * Constructor with parameter.
     *
     * @param networkService
     */
    public CompleteComponentRegistrationNotificationEventHandler(AbstractNetworkService networkService) {
        super(networkService);
    }
}
