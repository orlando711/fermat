package com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.home;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.expandableRecicler.ExpandableRecyclerAdapter;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletExpandableListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.util.FermatDividerItemDecoration;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.ContractBasicInformation;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletModuleManager;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters.OpenContractsExpandableAdapter;
import com.bitdubai.reference_wallet.crypto_broker_wallet.common.models.GrouperItem;
import com.bitdubai.reference_wallet.crypto_broker_wallet.util.CommonLogger;

import java.util.ArrayList;
import java.util.List;

import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_CONTRACT_UPDATE_VIEW;
import static com.bitdubai.fermat_cbp_api.all_definition.constants.CBPBroadcasterConstants.CBW_NEGOTIATION_UPDATE_VIEW;


/**
 * A simple {@link Fragment} subclass.
 */
public class OpenContractsTabFragment extends FermatWalletExpandableListFragment<GrouperItem, ReferenceAppFermatSession<CryptoBrokerWalletModuleManager>, ResourceProviderManager>
        implements FermatListItemListeners<ContractBasicInformation> {

    // Fermat Managers
    private CryptoBrokerWalletModuleManager moduleManager;
    private ErrorManager errorManager;

    private View emptyListViewsContainer;

    // Data
    private ArrayList<GrouperItem<ContractBasicInformation>> openContractList;


    public static OpenContractsTabFragment newInstance() {
        return new OpenContractsTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            moduleManager = appSession.getModuleManager();
            errorManager = appSession.getErrorManager();
        } catch (Exception ex) {
            CommonLogger.exception(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(
                        Wallets.CBP_CRYPTO_BROKER_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }

        onRefresh();
    }


    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        configureToolbar();

        RecyclerView.ItemDecoration itemDecoration = new FermatDividerItemDecoration(getActivity(), R.drawable.cbw_divider_shape);
        recyclerView.addItemDecoration(itemDecoration);
        emptyListViewsContainer = layout.findViewById(R.id.empty);
        FermatTextView emptyText = (FermatTextView) layout.findViewById(R.id.cbw_empty_message);
        ImageView emptyImage = (ImageView) layout.findViewById(R.id.cbw_empty_image);
        emptyText.setText(R.string.no_new_open_contracts);
        emptyImage.setImageResource(R.drawable.cbw_no_contracts);
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setBackground(getResources().getDrawable(R.drawable.cbw_action_bar_gradient_colors, null));
        else
            toolbar.setBackground(getResources().getDrawable(R.drawable.cbw_action_bar_gradient_colors));

        toolbar.setTitleTextColor(Color.WHITE);
        if (toolbar.getMenu() != null) toolbar.getMenu().clear();
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    public ExpandableRecyclerAdapter getAdapter() {
        if (adapter == null) {
            adapter = new OpenContractsExpandableAdapter(getActivity(), openContractList);
            // setting up event listeners
            adapter.setChildItemFermatEventListeners(this);
        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(getActivity());

        return layoutManager;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.cbw_fragment_open_negotiations_and_contracts_tab;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.cbw_open_negotiations_and_contracts_recycler_view;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    public List<GrouperItem> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        ArrayList<GrouperItem> data = new ArrayList<>();
        String grouperText;

        if (moduleManager != null) {
            GrouperItem<ContractBasicInformation> grouper;
            List<ContractBasicInformation> waitingForCustomer = new ArrayList<>();
            List<ContractBasicInformation> waitingForBroker = new ArrayList<>();

            try {

                waitingForBroker.addAll(moduleManager.getContractsWaitingForBroker(10, 0));
                waitingForCustomer.addAll(moduleManager.getContractsWaitingForCustomer(10, 0));

                if (!waitingForCustomer.isEmpty() || !waitingForBroker.isEmpty()) {
                    grouperText = getActivity().getString(R.string.waiting_for_you);
                    grouper = new GrouperItem<>(grouperText, waitingForBroker, true);
                    data.add(grouper);

                    grouperText = getActivity().getString(R.string.waiting_for_the_customer);
                    grouper = new GrouperItem<>(grouperText, waitingForCustomer, true);
                    data.add(grouper);
                }

            } catch (Exception ex) {
                CommonLogger.exception(TAG, ex.getMessage(), ex);
                if (errorManager != null) {
                    errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
                }
            }
        } else {
            Toast.makeText(getActivity(), "Sorry, an error happened in OpenContractsTabFragment (Module == null)", Toast.LENGTH_SHORT).show();
        }

        return data;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }

    @Override
    public void onItemClickListener(ContractBasicInformation data, int position) {
        appSession.setData("contract_data", data);
        changeActivity(Activities.CBP_CRYPTO_BROKER_WALLET_CONTRACT_DETAILS, appSession.getAppPublicKey());
    }

    @Override
    public void onLongItemClickListener(ContractBasicInformation data, int position) {
    }

    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                openContractList = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(openContractList);
            }
        }
        showOrHideEmptyView();
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public void onUpdateViewOnUIThread(String code) {
        switch (code) {
            case CBW_CONTRACT_UPDATE_VIEW:
                onRefresh();
                break;
            case CBW_NEGOTIATION_UPDATE_VIEW:
                onRefresh();
                break;
        }
    }

    private void showOrHideEmptyView()
    {
        if (openContractList == null || openContractList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyListViewsContainer.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyListViewsContainer.setVisibility(View.GONE);
        }
    }
}

