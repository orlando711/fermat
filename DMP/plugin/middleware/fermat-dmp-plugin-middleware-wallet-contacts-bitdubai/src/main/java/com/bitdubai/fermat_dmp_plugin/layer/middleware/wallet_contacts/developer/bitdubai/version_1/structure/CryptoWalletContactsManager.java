package com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.WalletContact;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.WalletContactsManager;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantCreateWalletContactException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantDeleteWalletContactException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetWalletContactException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantUpdateWalletContactException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetAllWalletContactsException;
import com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.exceptions.CantInitializeCryptoWalletContactsDatabaseException;

import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.structure.CryptoWalletContactsManager</code>
 * haves all consumable methods from the plugin
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 05/06/15.
 * @version 1.0
 */
public class CryptoWalletContactsManager implements DealsWithErrors, DealsWithPluginDatabaseSystem, DealsWithPluginIdentity, WalletContactsManager {

    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;

    /**
     * DealsWithPluginDatabaseSystem Interface member variables.
     */
    PluginDatabaseSystem pluginDatabaseSystem;

    /**
     * DealsWithPluginIdentity Interface member variable
     */
    private UUID pluginId;

    /*
     * Represents the DAO i'll be working with
     */
    CryptoWalletContactsDao cryptoWalletContactsDao;


    public void initialize() throws CantInitializeCryptoWalletContactsDatabaseException {
        /**
         * I will try to create and initialize a new  DAO
         */
        cryptoWalletContactsDao = new CryptoWalletContactsDao(errorManager, pluginDatabaseSystem);
        cryptoWalletContactsDao.initializeDatabase(pluginId, pluginId.toString());

    }

    /**
     * bring all contacts from the selected wallet
     * @param walletId wallet's id
     */
    @Override
    public List<WalletContact> listWalletContacts(UUID walletId) throws CantGetAllWalletContactsException {
        List<WalletContact> walletContacts;
        try {

            walletContacts = cryptoWalletContactsDao.findAll(walletId);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetAllWalletContactsException(e.getMessage());
        }
        return walletContacts;
    }

    /**
     * bring all contacts from the selected wallet limited by parameter
     * @param walletId wallet's id
     * @param max number of entities to get
     * @param offset position in the list for pagination
     * @return List<WalletContact> walletsContacts with all attributes
     * @throws CantGetAllWalletContactsException
     */
    @Override
    public List<WalletContact> listWalletContactsScrolling(UUID walletId, Integer max, Integer offset) throws CantGetAllWalletContactsException {
        List<WalletContact> walletContacts;
        try {

            walletContacts = cryptoWalletContactsDao.findAllScrolling(walletId, max, offset);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetAllWalletContactsException(e.getMessage());
        }
        return walletContacts;
    }

    /**
     * This method create a contact
     * @param actorId actor's id
     * @param actorName actor's name
     * @param actorType actor's actorType (extra-intra-device)
     * @param receivedCryptoAddress received cryptoAddress
     * @param walletId wallet's id
     * @return WalletContact
     */
    @Override
    public WalletContact createWalletContact(UUID actorId, String actorName, Actors actorType, CryptoAddress receivedCryptoAddress, UUID walletId) throws CantCreateWalletContactException {

        WalletContact walletContact;

        try {
            UUID contactId = UUID.randomUUID();

            walletContact = new CryptoWalletContact(actorId, actorName, actorType, contactId, receivedCryptoAddress, walletId);

            cryptoWalletContactsDao.create(walletContact);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCreateWalletContactException(e.getMessage());
        }

        return walletContact;
    }

    @Override
    public void updateWalletContact(UUID contactId, CryptoAddress receivedCryptoAddress, String actorName) throws CantUpdateWalletContactException {
        try {
            WalletContact walletContact = new CryptoWalletContact(contactId, receivedCryptoAddress, actorName);
            cryptoWalletContactsDao.update(walletContact);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateWalletContactException(e.getMessage());
        }
    }

    @Override
    public void deleteWalletContact(UUID contactId) throws CantDeleteWalletContactException {
        try {

            cryptoWalletContactsDao.delete(contactId);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantDeleteWalletContactException(e.getMessage());
        }
    }

    @Override
    public WalletContact getWalletContactByNameAndWalletId(String actorName, UUID walletId) throws CantGetWalletContactException {
        WalletContact walletContact;
        try {

            walletContact = cryptoWalletContactsDao.findByNameAndWalletId(actorName, walletId);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetWalletContactException(e.getMessage());
        }
        return walletContact;
    }

    @Override
    public WalletContact getWalletContactByNameContainsAndWalletId(String actorName, UUID walletId) throws CantGetWalletContactException {
        WalletContact walletContact;
        try {

            walletContact = cryptoWalletContactsDao.findByNameContainsAndWalletId(actorName, walletId);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetWalletContactException(e.getMessage());
        }
        return walletContact;
    }

    @Override
    public WalletContact getWalletContactByContactId(UUID contactId) throws CantGetWalletContactException {
        WalletContact walletContact;
        try {

            walletContact = cryptoWalletContactsDao.findById(contactId);

        } catch (Exception e){
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_WALLET_CONTACTS_MIDDLEWARE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantGetWalletContactException(e.getMessage());
        }
        return walletContact;
    }


    /**
     * DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * DealsWithPluginIdentity Interface implementation.
     */
    @Override
    public void setPluginId(UUID pluginId) {
        this.pluginId = pluginId;
    }

}