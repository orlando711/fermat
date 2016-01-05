package com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.interfaces;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.world.exceptions.CantGetIndexException;
import com.bitdubai.fermat_cbp_api.all_definition.contract.ContractClause;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractClauseStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ContractStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.CurrencyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ReferenceCurrency;
import com.bitdubai.fermat_cbp_api.all_definition.negotiation.Clause;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.enums.ContractType;
import com.bitdubai.fermat_cbp_api.layer.business_transaction.open_contract.exceptions.CantOpenContractException;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_purchase.interfaces.CustomerBrokerPurchaseNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.world.interfaces.FiatIndex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 27/11/15.
 */
public abstract class AbstractOpenContract {


    /**
     * Represents the contract Type, this type will use to select the actors involved in the
     * contract generation and the network service communication
     */
    public ContractType contractType;

    private ContractSaleRecord createContractRecordFromNegotiationClauses(
            Collection<Clause> negotiationClauses,
            FiatIndex fiatIndex,
            String brokerPublicKey,
            String customerPublicKey,
            String negotiationId,
            boolean nearExpirationDatetime)
            throws InvalidParameterException, CantGetIndexException {

        ContractSaleRecord contractRecord=new ContractSaleRecord();
        CurrencyType merchandiseCurrency;
        float merchandiseAmount;
        long merchandiseDeliveryExpirationDate;
        float paymentAmount;
        CurrencyType paymentCurrency;
        long paymentExpirationDate;
        String clauseValue;
        long dayTime;

        //Contract clauses
        Collection<ContractClause> contractClauses=new ArrayList<>();
        ContractClause contractClause;
        ClauseType clauseType;
        for(Clause clause : negotiationClauses){
            clauseValue=clause.getValue();
            clauseType=clause.getType();
            switch (clauseType){

                case BROKER_CURRENCY:
                    merchandiseCurrency=CurrencyType.getByCode(clauseValue);
                    contractRecord.setMerchandiseCurrency(merchandiseCurrency);
                    break;
                case BROKER_CURRENCY_QUANTITY:
                    merchandiseAmount=parseToFloat(clauseValue);
                    contractRecord.setMerchandiseAmount(merchandiseAmount);
                    break;
                case BROKER_DATE_TIME_TO_DELIVER:
                    merchandiseDeliveryExpirationDate=parseToLong(clauseValue);
                    contractRecord.setMerchandiseDeliveryExpirationDate(merchandiseDeliveryExpirationDate);
                    break;
                case CUSTOMER_CURRENCY_QUANTITY:
                    paymentAmount=parseToLong(clauseValue);
                    contractRecord.setPaymentAmount(paymentAmount);
                    break;
                case CUSTOMER_CURRENCY:
                    paymentCurrency=CurrencyType.getByCode(clauseValue);
                    contractRecord.setPaymentCurrency(paymentCurrency);
                    break;
                case CUSTOMER_DATE_TIME_TO_DELIVER:
                    paymentExpirationDate=parseToLong(clauseValue);
                    contractRecord.setPaymentExpirationDate(paymentExpirationDate);
                    break;
                case CUSTOMER_PAYMENT_METHOD:
                    contractClause=createContractClause(clauseValue);
                    contractClauses.add(contractClause);
                    break;
                case BROKER_PAYMENT_METHOD:
                    contractClause=createContractClause(clauseValue);
                    contractClauses.add(contractClause);
                    break;
                /*
                case DATE_TIME_TO_MEET:
                    dayTime=Long.valueOf(clauseValue);
                    contractRecord.setDayTime(dayTime);
                    */
            }
        }
        //TODO: I'm gonna set the dollar as reference currency for now, it can change in the future.
        float referencePrice = (float) fiatIndex.getPurchasePrice();
        contractRecord.setNegotiationId(negotiationId);
        contractRecord.setPublicKeyBroker(brokerPublicKey);
        contractRecord.setPublicKeyCustomer(customerPublicKey);
        //TODO: I'm gonna set the dollar as reference currency for now, it can change in the future.
        contractRecord.setReferenceCurrency(ReferenceCurrency.DOLLAR);
        contractRecord.setReferencePrice(referencePrice);
        contractRecord.setStatus(ContractStatus.PENDING_PAYMENT);
        //Sets the contractId (hash)
        contractRecord.generateContractHash();
        //To avoid a null field I gonna set the dayTime to 0
        //TODO: find the correct value, this was removed from negotiation clauses
        dayTime=0;
        contractRecord.setDayTime(dayTime);
        //New fields
        contractRecord.setNearExpirationDatetime(nearExpirationDatetime);
        contractRecord.setContractClauses(contractClauses);
        return contractRecord;
    }

    private ContractPurchaseRecord createContractPurchaseRecordFromNegotiationClauses(
            Collection<Clause> negotiationClauses,
            FiatIndex fiatIndex,
            String brokerPublicKey,
            String customerPublicKey,
            String negotiationId,
            boolean nearExpirationDatetime)
            throws InvalidParameterException, CantGetIndexException {

        ContractPurchaseRecord contractRecord=new ContractPurchaseRecord();
        CurrencyType merchandiseCurrency;
        float merchandiseAmount;
        long merchandiseDeliveryExpirationDate;
        float paymentAmount;
        CurrencyType paymentCurrency;
        long paymentExpirationDate;
        String clauseValue;
        long dayTime;

        //Contract clauses
        Collection<ContractClause> contractClauses=new ArrayList<>();
        ContractClause contractClause;
        ClauseType clauseType;

        for(Clause clause : negotiationClauses){
            clauseValue=clause.getValue();
            clauseType=clause.getType();
            switch (clauseType){

                case BROKER_CURRENCY:
                    merchandiseCurrency=CurrencyType.getByCode(clauseValue);
                    contractRecord.setMerchandiseCurrency(merchandiseCurrency);
                    break;
                case BROKER_CURRENCY_QUANTITY:
                    merchandiseAmount=parseToFloat(clauseValue);
                    contractRecord.setMerchandiseAmount(merchandiseAmount);
                    break;
                case BROKER_DATE_TIME_TO_DELIVER:
                    merchandiseDeliveryExpirationDate=parseToLong(clauseValue);
                    contractRecord.setMerchandiseDeliveryExpirationDate(merchandiseDeliveryExpirationDate);
                    break;
                case CUSTOMER_CURRENCY_QUANTITY:
                    paymentAmount=parseToLong(clauseValue);
                    contractRecord.setPaymentAmount(paymentAmount);
                    break;
                case CUSTOMER_CURRENCY:
                    paymentCurrency=CurrencyType.getByCode(clauseValue);
                    contractRecord.setPaymentCurrency(paymentCurrency);
                    break;
                case CUSTOMER_DATE_TIME_TO_DELIVER:
                    paymentExpirationDate=parseToLong(clauseValue);
                    contractRecord.setPaymentExpirationDate(paymentExpirationDate);
                    break;
                case CUSTOMER_PAYMENT_METHOD:
                    contractClause=createContractClause(clauseValue);
                    contractClauses.add(contractClause);
                    break;
                case BROKER_PAYMENT_METHOD:
                    contractClause=createContractClause(clauseValue);
                    contractClauses.add(contractClause);
                    break;
                /*
                case DATE_TIME_TO_MEET:
                    dayTime=Long.valueOf(clauseValue);
                    contractRecord.setDayTime(dayTime);
                    */
            }
        }

        //TODO: I'm gonna set the dollar as reference currency for now, it can change in the future.
        float referencePrice = (float) fiatIndex.getPurchasePrice();
        contractRecord.setNegotiationId(negotiationId);
        contractRecord.setPublicKeyBroker(brokerPublicKey);
        contractRecord.setPublicKeyCustomer(customerPublicKey);
        //TODO: I'm gonna set the dollar as reference currency for now, it can change in the future.
        contractRecord.setReferenceCurrency(ReferenceCurrency.DOLLAR);
        contractRecord.setReferencePrice(referencePrice);
        contractRecord.setStatus(ContractStatus.PENDING_PAYMENT);
        //Sets the contractId (hash)
        contractRecord.generateContractHash();
        contractRecord.setContractClauses(contractClauses);
        //New Field
        contractRecord.setNearExpirationDatetime(nearExpirationDatetime);
        return contractRecord;
    }

    private ContractClause createContractClause(String clauseValue) throws InvalidParameterException {
        ContractClauseRecord contractClause=new ContractClauseRecord();
        Integer executionOrder=616;
        UUID clauseId=UUID.randomUUID();
        contractClause.setClauseId(clauseId);
        ContractClauseType contractClauseType =ContractClauseType.getByCode(clauseValue);
        contractClause.setType(contractClauseType);
        contractClause.setExecutionOrder(executionOrder);
        contractClause.setStatus(ContractClauseStatus.PENDING);
        return contractClause;
    }

    /**
     * This method creates a ContractSaleRecord for purchase with given Negotiation clauses.
     * @param negotiationClauses
     * @return
     * @throws InvalidParameterException
     */
    public ContractPurchaseRecord createPurchaseContractRecord(Collection<Clause> negotiationClauses,
                                               CustomerBrokerPurchaseNegotiation customerBrokerPurchaseNegotiation,
                                               FiatIndex fiatIndex)
            throws InvalidParameterException,
            CantGetIndexException {

        String brokerPublicKey=customerBrokerPurchaseNegotiation.getBrokerPublicKey();
        String customerPublicKey=customerBrokerPurchaseNegotiation.getCustomerPublicKey();
        String negotiationId=customerBrokerPurchaseNegotiation.getNegotiationId().toString();
        ContractPurchaseRecord contractRecord= createContractPurchaseRecordFromNegotiationClauses(
                negotiationClauses,
                fiatIndex,
                brokerPublicKey,
                customerPublicKey,
                negotiationId,
                customerBrokerPurchaseNegotiation.getNearExpirationDatetime());
        return contractRecord;

    }

    /**
     * This method creates a ContractSaleRecord for purchase with given Negotiation clauses.
     * @param negotiationClauses
     * @return
     * @throws InvalidParameterException
     */
    public ContractSaleRecord createSaleContractRecord(Collection<Clause> negotiationClauses,
                                                       CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation,
                                                       FiatIndex fiatIndex)
            throws InvalidParameterException,
            CantGetIndexException {

        String brokerPublicKey=customerBrokerSaleNegotiation.getBrokerPublicKey();
        String customerPublicKey=customerBrokerSaleNegotiation.getCustomerPublicKey();
        String negotiationId=customerBrokerSaleNegotiation.getNegotiationId().toString();
        ContractSaleRecord contractRecord= createContractRecordFromNegotiationClauses(
                negotiationClauses,
                fiatIndex,
                brokerPublicKey,
                customerPublicKey,
                negotiationId,
                customerBrokerSaleNegotiation.getNearExpirationDatetime());

        return contractRecord;

    }

    /**
     * This method open a new contract
     * @param negotiationId
     * @throws CantOpenContractException
     */
    //public abstract void openContract(String negotiationId)throws CantOpenContractException;

    /**
     * This method parse a String object to a float object
     * @param stringValue
     * @return
     * @throws InvalidParameterException
     */
    public float parseToFloat(String stringValue) throws InvalidParameterException {
        if(stringValue==null){
            throw new InvalidParameterException("Cannot parse a null string value to float");
        }else{
            try{
                return Float.valueOf(stringValue);
            }catch (Exception exception){
                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE,
                        FermatException.wrapException(exception),
                        "Paring String object to float",
                        "Cannot parse string value to float");
            }

        }
    }

    /**
     * This method parse a String object to a long object
     * @param stringValue
     * @return
     * @throws InvalidParameterException
     */
    public long parseToLong(String stringValue) throws InvalidParameterException {
        if(stringValue==null){
            throw new InvalidParameterException("Cannot parse a null string value to long");
        }else{
            try{
                return Long.valueOf(stringValue);
            }catch (Exception exception){
                throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE,
                        FermatException.wrapException(exception),
                        "Parsing String object to long",
                        "Cannot parse string value to long");
            }

        }
    }

}
