package com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.interfaces;

import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.interfaces.FermatSettings;
import com.bitdubai.fermat_api.layer.modules.interfaces.ModuleManager;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantCreateNewIdentityAssetUserException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantGetAssetUserIdentitiesException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantListAssetUsersException;
import com.bitdubai.fermat_dap_api.layer.dap_identity.asset_user.exceptions.CantUpdateIdentityAssetUserException;

import java.util.List;

/**
 * Created by Nerio on 07/09/15.
 */
public interface IdentityAssetUserManager extends ModuleManager<FermatSettings, ActiveActorIdentityInformation> {

    /**
     * The method <code>getAllIntraWalletUsersFromCurrentDeviceUser</code> will give us a list of all the intra wallet users associated to the actual Device User logged in
     *
     * @return the list of Asset Issuer users associated to the current logged in Device User.
     * @throws CantListAssetUsersException if something goes wrong.
     */
    List<IdentityAssetUser> getIdentityAssetUsersFromCurrentDeviceUser() throws CantListAssetUsersException;

    /**
     * The method <code>getIdentityAssetIssuer</code> will give Identity Asset Issuer associated
     *
     * @return Identity Asset User associated.
     * @throws CantGetAssetUserIdentitiesException if something goes wrong.
     */
    IdentityAssetUser getIdentityAssetUser() throws CantGetAssetUserIdentitiesException;

    /**
     * The method <code>createNewIntraWalletUser</code> creates a new intra wallet user Identity for the logged in Device User and returns the
     * associated public key
     *
     * @param alias        the alias that the user choose as intra user identity
     * @param profileImage the profile image to identify this identity
     * @return the intra user created
     * @throws CantCreateNewIdentityAssetUserException if something goes wrong.
     */
    IdentityAssetUser createNewIdentityAssetUser(String alias,
                                                 byte[] profileImage) throws CantCreateNewIdentityAssetUserException;

    /**
     * The method <code>updateIdentityAssetUser</code> change a identity information data
     *
     * @param identityPublicKey
     * @param identityAlias
     * @param profileImage
     * @throws CantUpdateIdentityAssetUserException
     */
    void updateIdentityAssetUser(String identityPublicKey, String identityAlias, byte[] profileImage) throws CantUpdateIdentityAssetUserException;

    /**
     * The method <code>hasAssetUserIdentity</code> returns if has a intra user identity created
     *
     * @return
     * @throws CantListAssetUsersException
     */
    boolean hasAssetUserIdentity() throws CantListAssetUsersException;

}
