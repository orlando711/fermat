/*
 * @#DigitalAssetMetadataTransmitMessageReceiverProcessor.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure.processors;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_dap_api.layer.all_definition.digital_asset.DigitalAssetMetadata;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DapEvenType;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DistributionStatus;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.enums.DigitalAssetMetadataTransactionType;
import com.bitdubai.fermat_dap_api.layer.dap_network_services.asset_transmission.exceptions.CantSendDigitalAssetMetadataException;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.AssetTransmissionPluginRoot;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure.AssetTransmissionJsonAttNames;
import com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure.DigitalAssetMetadataTransactionImpl;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.google.gson.JsonObject;

/**
 * The Class <code>com.bitdubai.fermat_dap_plugin.layer.network.service.asset.transmission.developer.bitdubai.version_1.structure.processors.DigitalAssetMetadataTransmitMessageReceiverProcessor</code> is
 * that implement the logic when a Digital Asset Metadata Transmit Message is Receiver
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class DigitalAssetMetadataTransmitMessageReceiverProcessor extends FermatMessageProcessor {

    /**
     * Constructor with parameters
     * @param assetTransmissionPluginRoot
     */
    public DigitalAssetMetadataTransmitMessageReceiverProcessor(AssetTransmissionPluginRoot assetTransmissionPluginRoot) {
        super(assetTransmissionPluginRoot);
    }

    /**
     * (non-javadoc)
     *
     * @see FermatMessageProcessor#processingMessage(FermatMessage, JsonObject)
     */
    @Override
    public void processingMessage(FermatMessage fermatMessage, JsonObject jsonMsjContent) {

        try {

            /*
             * Get the XML representation of the Digital Asset Metadata
             */
            String digitalAssetMetadataXml     = jsonMsjContent.get(AssetTransmissionJsonAttNames.DIGITAL_ASSET_METADATA).getAsString();
            PlatformComponentType senderType   = gson.fromJson(jsonMsjContent.get(AssetTransmissionJsonAttNames.SENDER_TYPE).getAsString(), PlatformComponentType.class);
            PlatformComponentType receiverType = gson.fromJson(jsonMsjContent.get(AssetTransmissionJsonAttNames.RECEIVER_TYPE).getAsString(), PlatformComponentType.class);

            /*
             * Convert the xml to object
             */
            DigitalAssetMetadata digitalAssetMetadata = (DigitalAssetMetadata) XMLParser.parseXML(digitalAssetMetadataXml, DigitalAssetMetadata.class);

            /*
             * Construct a new digitalAssetMetadataTransaction
             */
            DigitalAssetMetadataTransactionImpl digitalAssetMetadataTransaction = new DigitalAssetMetadataTransactionImpl();
            digitalAssetMetadataTransaction.setGenesisTransaction(digitalAssetMetadata.getGenesisTransaction());
            digitalAssetMetadataTransaction.setSenderId(fermatMessage.getSender());
            digitalAssetMetadataTransaction.setSenderType(senderType);
            digitalAssetMetadataTransaction.setReceiverId(fermatMessage.getReceiver());
            digitalAssetMetadataTransaction.setReceiverType(receiverType);
            digitalAssetMetadataTransaction.setDigitalAssetMetadata(digitalAssetMetadata);
           // digitalAssetMetadataTransaction.setDistributionStatus(DistributionStatus.CRYPTO_RECEIVED); TODO: REVISAR STATUS
            digitalAssetMetadataTransaction.setType(DigitalAssetMetadataTransactionType.META_DATA_TRANSMIT);
            digitalAssetMetadataTransaction.setProcessed(DigitalAssetMetadataTransactionImpl.PROCESSED_NO);

            /*
             * Save into data base for audit control
             */
            getAssetTransmissionPluginRoot().getDigitalAssetMetaDataTransactionDao().create(digitalAssetMetadataTransaction);

            /*
             * Mark the message as read
             */
            ((FermatMessageCommunication)fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.READ);
            ((CommunicationNetworkServiceConnectionManager)getAssetTransmissionPluginRoot().getNetworkServiceConnectionManager()).getIncomingMessageDao().update(fermatMessage);

            /*
             * Notify to the interested
             */
            FermatEvent event =  getAssetTransmissionPluginRoot().getEventManager().getNewEvent(DapEvenType.RECEIVED_NEW_DIGITAL_ASSET_METADATA_NOTIFICATION);
            event.setSource(AssetTransmissionPluginRoot.EVENT_SOURCE);
            getAssetTransmissionPluginRoot().getEventManager().raiseEvent(event);

        } catch (Exception e) {
            getAssetTransmissionPluginRoot().getErrorManager().reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_TRANSMISSION_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }

    }

    /**
     * (non-javadoc)
     *
     * @see FermatMessageProcessor#getDigitalAssetMetadataTransactionType()
     */
    @Override
    public DigitalAssetMetadataTransactionType getDigitalAssetMetadataTransactionType() {
        return DigitalAssetMetadataTransactionType.META_DATA_TRANSMIT;
    }
}
