package com.bitdubai.fermat_dap_plugin.layer.digital_asset_transaction.asset_issuing.developer.bitdubai.version_1.developer_utils.mocks;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_issuer.exceptions.CantSingMessageException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_issuer.interfaces.IdentityAssetIssuer;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 25/09/15.
 */
public class MockIdentityAssetIssuerForTest implements IdentityAssetIssuer {
    @Override
    public String getAlias() {
        return "Franklin Marcano";
    }

    @Override
    public String getPublicKey() {
        return "ASDS-10087982";
    }

    /**
     * @return an element of Actors enum representing the type of the actor identity.
     */
    @Override
    public Actors getActorType() {
        return Actors.DAP_ASSET_ISSUER;
    }

    @Override
    public byte[] getImage() {
        return new byte[0];
    }

    @Override
    public void setNewProfileImage(byte[] newProfileImage) {

    }

    @Override
    public String createMessageSignature(String mensage)  {
        return "signature";
    }
}
