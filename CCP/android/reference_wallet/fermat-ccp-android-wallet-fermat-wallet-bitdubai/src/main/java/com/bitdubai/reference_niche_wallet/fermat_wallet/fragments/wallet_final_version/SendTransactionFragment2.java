package com.bitdubai.reference_niche_wallet.fermat_wallet.fragments.wallet_final_version;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_wallet_fermat.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.CircularProgressBar;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatAnimationsUtils;
import com.bitdubai.fermat_android_api.ui.util.FermatDividerItemDecoration;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.enums.VaultType;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.modules.common_classes.ActiveActorIdentityInformation;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.TransactionType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.fermat_wallet.interfaces.FermatWalletTransaction;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.FermatWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.CantCreateWalletContactException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.CantFindWalletContactException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.CantGetBalanceException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.CantRequestFermatAddressException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.ContactNameAlreadyExistsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.exceptions.WalletContactNotFoundException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.interfaces.FermatWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.interfaces.FermatWalletModuleTransaction;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.fermat_wallet.interfaces.FermatWalletWalletContact;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.ExchangeRateProvider;
import com.bitdubai.fermat_cer_api.all_definition.interfaces.ExchangeRate;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.adapters.ReceivetransactionsAdapter;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.FermatWalletConstants;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.animation.AnimationManager;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.popup.BlockchainDownloadInfoDialog;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.popup.PresentationBitcoinWalletDialog;
import com.bitdubai.reference_niche_wallet.fermat_wallet.common.utils.WalletUtils;

import com.bitdubai.reference_niche_wallet.fermat_wallet.session.SessionConstant;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.widget.Toast.makeText;


/**
 * Fragment the show the list of open negotiations waiting for the broker and the customer un the Home activity
 *
 * @author MAtias Furszyfer
 */

public class SendTransactionFragment2 extends FermatWalletListFragment<FermatWalletModuleTransaction,ReferenceAppFermatSession<FermatWallet>,ResourceProviderManager>
        implements FermatListItemListeners<FermatWalletTransaction> {




    private  BlockchainNetworkType blockchainNetworkType;
    private long before = 0;
    private long after = 0;
    private boolean pressed = false;
    private CircularProgressBar circularProgressBar;
    private Thread background;

    // Fermat Managers
    private FermatWallet moduleManager;
    private List<FermatWalletModuleTransaction> lstFermatWalletTransactions;
    private TextView txt_type_balance;
    private TextView txt_balance_amount;
    private TextView txt_Date_time;
    private TextView txt_rate_amount;
    private long balanceAvailable;
    private View rootView;
    private List<FermatWalletModuleTransaction> lstCryptoWalletTransactionsAvailable;
    private long bookBalance;
    private LinearLayout emptyListViewsContainer;
    private AnimationManager animationManager;
    private TextView txt_type_balance_amount;
    private int progress1=1;
    private Map<Long, Long> runningDailyBalance;
    final Handler handler = new Handler();


    private UUID exchangeProviderId = null;

    private FermatWalletSettings fermatWalletSettings = null;

    private ExecutorService _executor;
    private BalanceType balanceType = BalanceType.AVAILABLE;
    private ShowMoneyType typeAmountSelected = ShowMoneyType.BITCOIN;

    public static SendTransactionFragment2 newInstance() {
        return new SendTransactionFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _executor = Executors.newFixedThreadPool(5);
        setHasOptionsMenu(true);

        lstCryptoWalletTransactionsAvailable = new ArrayList<>();
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    final Drawable drawable = getResources().getDrawable(R.drawable.background_gradient, null);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getPaintActivtyFeactures().setActivityBackgroundColor(drawable);
                            } catch (OutOfMemoryError o) {
                                o.printStackTrace();
                            }
                        }
                    });
                }

            }
        });

        try {

            moduleManager = appSession.getModuleManager();


            if(appSession.getData(SessionConstant.TYPE_BALANCE_SELECTED) != null)
                balanceType = (BalanceType)appSession.getData(SessionConstant.TYPE_BALANCE_SELECTED);
            else
                appSession.setData(SessionConstant.TYPE_BALANCE_SELECTED, balanceType);

            if(appSession.getData(SessionConstant.TYPE_AMOUNT_SELECTED) != null)
                typeAmountSelected = (ShowMoneyType)appSession.getData(SessionConstant.TYPE_AMOUNT_SELECTED);
            else
                appSession.setData(SessionConstant.TYPE_AMOUNT_SELECTED, typeAmountSelected);

            //get wallet settings
            try {
                fermatWalletSettings = moduleManager.loadAndGetSettings(appSession.getAppPublicKey());
            } catch (Exception e) {
                fermatWalletSettings = null;
            }

            if (fermatWalletSettings == null) {
                fermatWalletSettings = new FermatWalletSettings();
                fermatWalletSettings.setIsContactsHelpEnabled(true);
                fermatWalletSettings.setIsPresentationHelpEnabled(true);
                fermatWalletSettings.setNotificationEnabled(true);
                fermatWalletSettings.setIsBlockchainDownloadEnabled(true);
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
                fermatWalletSettings.setBlockchainNetworkType(blockchainNetworkType);
                if(moduleManager!=null)
                    moduleManager.persistSettings(appSession.getAppPublicKey(), fermatWalletSettings);
            } else {
                if (fermatWalletSettings.getBlockchainNetworkType() == null)
                    fermatWalletSettings.setBlockchainNetworkType(BlockchainNetworkType.getDefaultBlockchainNetworkType());
                else
                    blockchainNetworkType = fermatWalletSettings.getBlockchainNetworkType();
            }

            try {
                if(moduleManager!=null) moduleManager.persistSettings(appSession.getAppPublicKey(), fermatWalletSettings);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final FermatWalletSettings bitcoinWalletSettingsTemp = fermatWalletSettings;
            _executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (bitcoinWalletSettingsTemp.isPresentationHelpEnabled()) {
                                    setUpPresentation(false);
                                }
                                setRunningDailyBalance();

                            }
                        }, 500);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            //list transaction on background

            onRefresh();

            //check blockchain progress

                       /*         try {
                                    int pendingBlocks = moduleManager.getBlockchainDownloadProgress(blockchainNetworkType).getPendingBlocks();
                                    final Toolbar toolBar = getToolbar();
                                    int toolbarColor = 0;
                                    if (pendingBlocks > 0) {
                                        //paint toolbar on red
                                        toolbarColor = Color.RED;
                                        if (fermatWalletSettings.isBlockchainDownloadEnabled())
                                            setUpBlockchainProgress(fermatWalletSettings.isBlockchainDownloadEnabled());
                                    } else {
                                        toolbarColor = Color.parseColor("#12aca1");
                                    }
                                    final int finalToolbarColor = toolbarColor;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            toolBar.setBackgroundColor(finalToolbarColor);
                                                             }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }*/




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void setUpPresentation(boolean checkButton) {
        PresentationBitcoinWalletDialog presentationBitcoinWalletDialog =
                new PresentationBitcoinWalletDialog(
                        getActivity(),
                        appSession,
                        null,
                        (moduleManager.getActiveIdentities().isEmpty()) ? PresentationBitcoinWalletDialog.TYPE_PRESENTATION : PresentationBitcoinWalletDialog.TYPE_PRESENTATION_WITHOUT_IDENTITIES,
                        checkButton);


        presentationBitcoinWalletDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Object o = appSession.getData(SessionConstant.PRESENTATION_IDENTITY_CREATED);
                if (o != null) {
                    if ((Boolean) (o))
                        appSession.removeData(SessionConstant.PRESENTATION_IDENTITY_CREATED);
                }
                //noinspection TryWithIdenticalCatches
                try {
                    ActiveActorIdentityInformation cryptoWalletIntraUserIdentity = moduleManager.getSelectedActorIdentity();
                    if (cryptoWalletIntraUserIdentity == null) {
                        getActivity().onBackPressed();
                    } else {
                        invalidate();
                    }

                } catch (CantGetSelectedActorIdentityException e) {
                    e.printStackTrace();
                } catch (ActorIdentityNotSelectedException e) {
                    e.printStackTrace();
                }

            }
        });
        presentationBitcoinWalletDialog.show();
    }

    private void setUpBlockchainProgress(final boolean checkButton) {

        final int type = (moduleManager.getActiveIdentities().isEmpty()) ? PresentationBitcoinWalletDialog.TYPE_PRESENTATION : PresentationBitcoinWalletDialog.TYPE_PRESENTATION_WITHOUT_IDENTITIES;


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BlockchainDownloadInfoDialog blockchainDownloadInfoDialog =
                        new BlockchainDownloadInfoDialog(
                                getActivity(),
                                appSession,
                                null,
                                type,
                                checkButton);


                blockchainDownloadInfoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                blockchainDownloadInfoDialog.show();
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            animationManager = new AnimationManager(rootView,emptyListViewsContainer);
            getPaintActivtyFeactures().addCollapseAnimation(animationManager);
        } catch (Exception e){
            makeText(getActivity(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            appSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        setUp(inflater);

        return rootView;
    }

    @Override
    public void onResume() {
        animationManager = new AnimationManager(rootView, emptyListViewsContainer);
        getPaintActivtyFeactures().addCollapseAnimation(animationManager);
        super.onResume();
    }

    @Override
    public void onStop() {
        getPaintActivtyFeactures().removeCollapseAnimation(animationManager);
        super.onStop();
    }

    private void setUp(LayoutInflater inflater){
        try {
           // setUpDonut(inflater);
            setUpHeader(inflater);
            setUpScreen();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpScreen(){
        int[] emptyOriginalPos = new int[2];
        if(emptyListViewsContainer!=null)
            emptyListViewsContainer.getLocationOnScreen(emptyOriginalPos);    }
    String runningBalance;

    private void setUpHeader(LayoutInflater inflater) throws CantGetBalanceException {
        final RelativeLayout container_header_balance = getToolbarHeader();
        try{
            container_header_balance.removeAllViews();
        }catch (Exception e){
            e.printStackTrace();
        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            container_header_balance.setBackgroundDrawable( getResources().getDrawable(R.drawable.background_white_gradient) );
        } else {
            container_header_balance.setBackground( getResources().getDrawable(R.drawable.background_white_gradient));
        }

        final View header_layout = inflater.inflate(R.layout.fermat_wallet_home_header,container_header_balance,true);
        container_header_balance.setVisibility(View.VISIBLE);



        //Select all header Element
        txt_balance_amount      = (TextView) header_layout.findViewById(R.id.txt_balance_amount);
        txt_type_balance_amount = (TextView) header_layout.findViewById(R.id.txt_type_balance_amount);
        txt_type_balance        = (TextView) header_layout.findViewById(R.id.txt_type_balance);
        txt_Date_time           = (TextView) header_layout.findViewById(R.id.txt_date_time);
        txt_rate_amount         = (TextView) header_layout.findViewById(R.id.txt_rate_amount);

        final String date;
        final String time;
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:ss a");

        date = sdf1.format(System.currentTimeMillis());
        time = sdf2.format(System.currentTimeMillis());

        txt_Date_time.setText(time+" | "+date);

        //Event Click For change the balance type
        txt_type_balance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeBalanceType(txt_type_balance, txt_balance_amount);
            }
        });

        //Event Click For change the balance type
        txt_type_balance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeBalanceType(txt_type_balance, txt_balance_amount);
            }
        });

        //Event for change the balance amount
        txt_balance_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeAmountType();
            }
        });
        //Event for change the balance amount type
        txt_type_balance_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeAmountType();
            }
        });



        long balance = 0;


        if (balanceType.equals(BalanceType.AVAILABLE))
            balance =  moduleManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(), blockchainNetworkType);
        else
            //balance = moduleManager.getRealBalance(appSession.getAppPublicKey(), blockchainNetworkType);

        txt_balance_amount.setText(WalletUtils.formatBalanceString(balance, typeAmountSelected.getCode()));

    }

   /* private void setUpDonut(LayoutInflater inflater)  {
        try {
            final RelativeLayout container_header_balance = getToolbarHeader();
            try {
                container_header_balance.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }

            container_header_balance.setBackgroundColor(Color.parseColor("#06356f"));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inScaled = true;
                    options.inSampleSize = 3;
                    try {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_header,options);
    //                    bitmap = Bitmap.createScaledBitmap(bitmap,300,400,true);
                        final Bitmap finalBitmap = bitmap;
                        if(finalBitmap!=null) {
                            Runnable runnableHandler = new Runnable() {
                                @Override
                                public void run() {
                                    container_header_balance.setBackground(new BitmapDrawable(getResources(), finalBitmap));
                                }
                            };
                            handler.post(runnableHandler);
                        }
                    }catch (OutOfMemoryError e){
                        e.printStackTrace();
                        System.gc();
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
            final View balance_header = inflater.inflate(R.layout.donut_header, container_header_balance, true);
            container_header_balance.setVisibility(View.VISIBLE);
            circularProgressBar = (CircularProgressBar) balance_header.findViewById(R.id.progress);

                _executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runningBalance = WalletUtils.formatBalanceStringNotDecimal(
                                    moduleManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(),
                                    blockchainNetworkType), ShowMoneyType.BITCOIN.getCode());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    circularProgressBar.setProgressValue(Integer.valueOf(runningBalance));
                                    circularProgressBar.setProgressValue2(getBalanceAverage());
                                    circularProgressBar.setBackgroundProgressColor(Color.parseColor("#022346"));
                                    circularProgressBar.setProgressColor(Color.parseColor("#05ddd2"));
                                    circularProgressBar.setProgressColor2(Color.parseColor("#05537c"));
                                }
                            });
                        } catch (CantGetBalanceException e) {
                            e.printStackTrace();
                        }

                    }
                });


            txt_type_balance = (TextView) balance_header.findViewById(R.id.txt_type_balance);

            // handler for the background updating
            final Handler progressHandler = new Handler() {
                public void handleMessage(Message msg) {
                    progress1++;
                    try {
                        circularProgressBar.setProgressValue(progress1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };

            TextView txt_amount_type = (TextView) balance_header.findViewById(R.id.txt_balance_amount_type);
            txt_type_balance.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        pressed = true;
                        before = System.currentTimeMillis();

                        //TODO fijatse que no se lancen mas de un hilo
                        if (pressed){
                            background = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        // enter the code to be run while displaying the progressbar.
                                        // This example is just going to increment the progress bar:
                                        // So keep running until the progress value reaches maximum value
                                        while (circularProgressBar.getprogress1() <= 300) {
                                            // wait 500ms between each update
                                            Thread.sleep(300);
                                            // System.out.println(circularProgressBar.getprogress1());
                                            // active the update handler
                                            progressHandler.sendMessage(progressHandler.obtainMessage());
                                        }
                                        pressed = false;
                                    } catch (java.lang.InterruptedException e) {
                                        // if something fails do something smart
                                    }
                                }
                            });
                            background.start();
                        }

                    return true;

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        pressed = false;
                        background.interrupt();
                        after = System.currentTimeMillis();
                        if (after - before < 2000) {
                            changeBalanceType(txt_type_balance, txt_balance_amount);
                            //System.out.println(System.currentTimeMillis());
                            circularProgressBar.setProgressValue(Integer.valueOf(runningBalance));
                            circularProgressBar.setProgressValue2(getBalanceAverage());
                            return true;
                        }else {
                            //String receivedAddress = GET("http://52.27.68.19:15400/mati/address/");
                            GET("",getActivity());
                            progress1 = 1;
                            circularProgressBar.setProgressValue(progress1);
                            return true;

                        }
                    }

                    return false;
                }
            });

            txt_balance_amount = (TextView) balance_header.findViewById(R.id.txt_balance_amount);
            txt_balance_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAmountType();
                }
            });

            txt_amount_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeAmountType();
                }
            });

            txt_balance_amount = (TextView) balance_header.findViewById(R.id.txt_balance_amount);
            try {
                _executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final long balance = moduleManager.getBalance(
                                    balanceType, appSession.getAppPublicKey(), blockchainNetworkType);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    txt_balance_amount.setText(WalletUtils.formatBalanceString(balance, typeAmountSelected.getCode()));
                                }
                            });
                        } catch (CantGetBalanceException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            txt_type_balance_amount = (FermatTextView) balance_header.findViewById(R.id.txt_balance_amount_type);
        }
        catch (Exception e){
            e.printStackTrace();
            // errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
        }
    }*/

    private String getWalletAddress(String actorPublicKey) {
        String walletAddress="";
        //noinspection TryWithIdenticalCatches
        try {
            CryptoAddress cryptoAddress = moduleManager.requestAddressToKnownUser(
                    moduleManager.getSelectedActorIdentity().getPublicKey(),
                    Actors.INTRA_USER,
                    actorPublicKey,
                    Actors.EXTRA_USER,
                    Platforms.CRYPTO_CURRENCY_PLATFORM,
                    VaultType.CRYPTO_CURRENCY_VAULT,
                    "BITV",
                    appSession.getAppPublicKey(),
                    ReferenceWallet.BASIC_WALLET_BITCOIN_WALLET,
                    blockchainNetworkType
            );
            walletAddress = cryptoAddress.getAddress();
        } catch (CantRequestFermatAddressException e) {
            // errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch (CantGetSelectedActorIdentityException e) {
            e.printStackTrace();
        } catch (ActorIdentityNotSelectedException e) {
            e.printStackTrace();
        }
        return walletAddress;
    }

    public void GET(@SuppressWarnings("UnusedParameters") String url, final Context context){
        final Handler mHandler = new Handler();
        try {
            if(moduleManager.getBalance(BalanceType.AVAILABLE,appSession.getAppPublicKey(),blockchainNetworkType)<500000000L) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String receivedAddress = "";
                        final HttpClient Client = new DefaultHttpClient();
                        //noinspection TryWithIdenticalCatches
                        try {
                            // Create Request to server and get response
                            String SetServerString;
                            HttpGet httpget = new HttpGet("http://52.27.68.19:15400/mati/address/");
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            SetServerString = Client.execute(httpget, responseHandler);
                            // Show response on activity

                            receivedAddress = SetServerString;
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final String finalReceivedAddress = receivedAddress;

                        String response = "";
                        try {
                            String SetServerString;
                            CryptoAddress cryptoAddress = new CryptoAddress(finalReceivedAddress, CryptoCurrency.BITCOIN);
                            FermatWalletWalletContact cryptoWalletWalletContact = null;
                            try {
                                cryptoWalletWalletContact = moduleManager.createWalletContact(
                                        cryptoAddress, "regtest_bitcoins", "", "", Actors.EXTRA_USER, appSession.getAppPublicKey(),blockchainNetworkType);
                            } catch (CantCreateWalletContactException | ContactNameAlreadyExistsException e) {
                                try {
                                    cryptoWalletWalletContact = moduleManager.findWalletContactByName(
                                            "regtest_bitcoins", appSession.getAppPublicKey(), moduleManager.getSelectedActorIdentity().getPublicKey());
                                } catch (CantFindWalletContactException |
                                        WalletContactNotFoundException  e3) {
                                    e.printStackTrace();
                                } catch (CantGetSelectedActorIdentityException e1) {
                                    e1.printStackTrace();
                                } catch (ActorIdentityNotSelectedException e1) {
                                    e1.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            assert cryptoWalletWalletContact != null;
                            String myCryptoAddress = getWalletAddress(cryptoWalletWalletContact.getActorPublicKey());
                            HttpGet httpget = new HttpGet("http://52.27.68.19:15400/mati/hello/?address=" + myCryptoAddress);
                            ResponseHandler<String> responseHandler = new BasicResponseHandler();
                            SetServerString = Client.execute(httpget, responseHandler);

                            response = SetServerString;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        final String finalResponse = response;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!finalResponse.equals("transaccion fallida"))
                                    Toast.makeText(context, "Regtest bitcoin arrived", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        } catch (CantGetBalanceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.add(0, FermatWalletConstants.IC_ACTION_SEND, 0, "send").setIcon(R.drawable.ic_actionbar_send)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        menu.add(1, FermatWalletConstants.IC_ACTION_HELP_PRESENTATION, 1, "help").setIcon(R.drawable.fw_help_icon)
               .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if(id == FermatWalletConstants.IC_ACTION_SEND){
                changeActivity(Activities.CCP_BITCOIN_FERMAT_WALLET_SEND_FORM_ACTIVITY,appSession.getAppPublicKey());
                return true;
            }else if(id == FermatWalletConstants.IC_ACTION_HELP_PRESENTATION){
                setUpPresentation(moduleManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
                return true;
            }
        } catch (Exception e) {
            // errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViews(View layout) {
        super.initViews(layout);

        RecyclerView.ItemDecoration itemDecoration = new FermatDividerItemDecoration(getActivity(), R.drawable.cbw_divider_shape);
        recyclerView.addItemDecoration(itemDecoration);

        if(lstFermatWalletTransactions!=null) {
            if (lstFermatWalletTransactions.isEmpty()) {
               recyclerView.setVisibility(View.GONE);
                emptyListViewsContainer = (LinearLayout) layout.findViewById(R.id.empty);
                FermatAnimationsUtils.showEmpty(getActivity(), true, emptyListViewsContainer);
                emptyListViewsContainer.setVisibility(View.VISIBLE);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyListViewsContainer = (LinearLayout) layout.findViewById(R.id.empty);
            FermatAnimationsUtils.showEmpty(getActivity(), true, emptyListViewsContainer);
            emptyListViewsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }


    @Override
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ReceivetransactionsAdapter(getActivity(), lstFermatWalletTransactions,getResources());
            // setting up event listeners
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
        return R.layout.fermat_wallet_home_send_main;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.open_contracts_recycler_view;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    public List<FermatWalletModuleTransaction> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) {
        List<FermatWalletModuleTransaction> data = new ArrayList<>();
        lstCryptoWalletTransactionsAvailable = new ArrayList<>();

        //noinspection TryWithIdenticalCatches
        try {
            ActiveActorIdentityInformation intraUserLoginIdentity = moduleManager.getSelectedActorIdentity();
            if(intraUserLoginIdentity!=null) {
                String intraUserPk = intraUserLoginIdentity.getPublicKey();


                int MAX_TRANSACTIONS = 20;
                List<FermatWalletModuleTransaction> list = moduleManager.listLastActorTransactionsByTransactionType(
                        BalanceType.AVAILABLE, TransactionType.DEBIT, appSession.getAppPublicKey(),
                        intraUserPk, blockchainNetworkType, MAX_TRANSACTIONS, 0);

                if(list!=null) {
                    lstCryptoWalletTransactionsAvailable.addAll(list);
                }

                if(!data.isEmpty())
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            FermatAnimationsUtils.showEmpty(getActivity(), true, emptyListViewsContainer);
                        }
                    });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }


    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                //noinspection unchecked
                lstFermatWalletTransactions = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(lstFermatWalletTransactions);

                if(lstFermatWalletTransactions.size() > 0)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    FermatAnimationsUtils.showEmpty(getActivity(), false, emptyListViewsContainer);
                }
            }
            else {
                recyclerView.setVisibility(View.GONE);
                FermatAnimationsUtils.showEmpty(getActivity(), true, emptyListViewsContainer);
            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            //errorManager.reportUnexpectedPluginException(Plugins.CRYPTO_WALLET, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, ex);
            ex.printStackTrace();
        }
    }

    private void changeAmountType() {
        ShowMoneyType showMoneyType = (typeAmountSelected.getCode()== ShowMoneyType.BITCOIN.getCode()) ? ShowMoneyType.BITS : ShowMoneyType.BITCOIN;
        appSession.setData(SessionConstant.TYPE_AMOUNT_SELECTED,showMoneyType);
        typeAmountSelected = showMoneyType;
        String moneyTpe = "";
        switch (showMoneyType){
            case BITCOIN:
                moneyTpe = "btc";
                txt_balance_amount.setTextSize(28);
                break;
            case BITS:
                moneyTpe = "bits";
                txt_balance_amount.setTextSize(20);
                break;
        }

        txt_type_balance_amount.setText(moneyTpe);
        updateBalances();
    }

    /**
     * Method to change the balance type
     */
    private void changeBalanceType(TextView txt_type_balance,TextView txt_balance_amount) {
        updateBalances();
        setRunningDailyBalance();
        try {


            if (balanceType.getCode().equals(BalanceType.AVAILABLE.getCode())) {

                balanceAvailable = loadBalance(BalanceType.AVAILABLE);
                txt_balance_amount.setText(WalletUtils.formatBalanceString(bookBalance, typeAmountSelected.getCode()));
                txt_type_balance.setText(R.string.book_balance);
                appSession.setData(SessionConstant.TYPE_BALANCE_SELECTED, BalanceType.BOOK);
                balanceType = BalanceType.BOOK;
            } else if (balanceType.getCode().equals(BalanceType.BOOK.getCode())) {
                bookBalance = loadBalance(BalanceType.BOOK);
               txt_balance_amount.setText(WalletUtils.formatBalanceString(balanceAvailable, typeAmountSelected.getCode()));
                txt_type_balance.setText(R.string.available_balance);
                balanceType = BalanceType.AVAILABLE;
                appSession.setData(SessionConstant.TYPE_BALANCE_SELECTED,BalanceType.AVAILABLE);
            }
        } catch (Exception e) {
            appSession.getErrorManager().reportUnexpectedUIException(
                    UISource.ACTIVITY, UnexpectedUIExceptionSeverity.CRASH, FermatException.wrapException(e));
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }

    }

    private long loadBalance(BalanceType balanceType){
        long balance = 0;
        //noinspection TryWithIdenticalCatches
        try {

            balance = moduleManager.getBalance(balanceType, appSession.getAppPublicKey(), blockchainNetworkType);

        } catch (CantGetBalanceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }


    private void updateBalances() {
        bookBalance = loadBalance(BalanceType.BOOK);
        balanceAvailable = loadBalance(BalanceType.AVAILABLE);
        txt_balance_amount.setText(
                WalletUtils.formatBalanceString(
                        (balanceType.getCode().equals(BalanceType.AVAILABLE.getCode()))
                        ? balanceAvailable : bookBalance, typeAmountSelected.getCode()));

    }


    private int getBalanceAverage() {
        long balanceSum = 0;
        int average = 0;
        try {
            if(runningDailyBalance!=null) {
                for (Map.Entry<Long, Long> entry : runningDailyBalance.entrySet())
                    balanceSum += Integer.valueOf(WalletUtils.formatBalanceStringNotDecimal(entry.getValue(), ShowMoneyType.BITCOIN.getCode()));

                if (balanceSum > 0)
                    average = (int) ((Integer.valueOf(WalletUtils.formatBalanceStringNotDecimal(getBalanceValue(runningDailyBalance.size() - 1), ShowMoneyType.BITCOIN.getCode())) * 100) / balanceSum);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return average;
    }

    private void setRunningDailyBalance() {
        try {

            long currentTime = System.currentTimeMillis();
            runningDailyBalance = new HashMap<>();

            if(fermatWalletSettings != null){

                blockchainNetworkType = fermatWalletSettings.getBlockchainNetworkType();
                if (fermatWalletSettings.getRunningDailyBalance() == null) {
                    try {
                        long balance = moduleManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(), blockchainNetworkType);
                        runningDailyBalance.put(currentTime, balance);
                    }catch (Exception e) {
                        Log.e(TAG,"Balance null, please check this, line:"+new Throwable().getStackTrace()[0].getLineNumber());
                    }
                } else {
                    runningDailyBalance = fermatWalletSettings.getRunningDailyBalance();

                    //verify that I have this day added
                    long lastDate = getKeyDate(runningDailyBalance.size()-1);
                    long dif = currentTime - lastDate;
                    double dias = Math.floor(dif / (1000 * 60 * 60 * 24));

                    if(dias > 1) {
                        //if I have 30 days I start counting again
                        if(runningDailyBalance.size() == 30)
                            runningDailyBalance = new HashMap<>();

                        runningDailyBalance.put(currentTime, moduleManager.getBalance(
                                BalanceType.AVAILABLE, appSession.getAppPublicKey(),blockchainNetworkType));
                    } else {
                        //update balance
                        this.updateDailyBalance(runningDailyBalance.size()-1,moduleManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(),blockchainNetworkType));
                    }
                }

                fermatWalletSettings.setRunningDailyBalance(runningDailyBalance);
                if(moduleManager!=null) {
                    moduleManager.persistSettings(appSession.getAppPublicKey(), fermatWalletSettings);
                }else {
                    Log.e(TAG,"Settings manager null, please check this line:"+new Throwable().getStackTrace()[0].getLineNumber());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getKeyDate(int pos){
        int i = 0;
        long date = 0;

        try {
            for (Map.Entry<Long, Long> entry :  runningDailyBalance.entrySet()) {
                if(i == pos)
                    date += entry.getKey();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private long getBalanceValue(int pos){
        int i = 0;
        long date = 0;

        try {
            for (Map.Entry<Long, Long> entry :  runningDailyBalance.entrySet()) {
                if(i == pos)
                    date += entry.getValue();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private long updateDailyBalance(int pos, long balance) {
        int i = 0;
        long date = 0;

        try {
            for (Map.Entry<Long, Long> entry :  runningDailyBalance.entrySet()) {
                if(i == pos) {
                    entry.setValue(balance);
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    @Override
    public void onUpdateViewOnUIThread(String code){
        try {
            if(code.equals("BlockchainDownloadComplete")) {
                //update toolbar color
                final Toolbar toolBar = getToolbar();

                toolBar.setBackgroundColor(Color.parseColor("#12aca1"));

               // makeText(getActivity(), "Blockchain Download Complete", Toast.LENGTH_SHORT).show();
            } else {
                if(code.equals("Btc_arrive"))
                {
                    //update balance amount
                    final String runningBalance = WalletUtils.formatBalanceStringNotDecimal(
                            moduleManager.getBalance(BalanceType.AVAILABLE, appSession.getAppPublicKey(),
                                    blockchainNetworkType),ShowMoneyType.BITCOIN.getCode());

                    changeBalanceType(txt_type_balance, txt_balance_amount);

                    circularProgressBar.setProgressValue(Integer.valueOf(runningBalance));
                    circularProgressBar.setProgressValue2(getBalanceAverage());
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onItemClickListener(FermatWalletTransaction data, int position) {

    }

    @Override
    public void onLongItemClickListener(FermatWalletTransaction data, int position) {

    }



   /* private void getAndShowMarketExchangeRateData(final View container) {

        final int MAX_DECIMAL_FOR_RATE = 2;
        final int MIN_DECIMAL_FOR_RATE = 2;

        FermatWorker fermatWorker = new FermatWorker(getActivity()) {
            @Override
            protected Object doInBackground()  {

                ExchangeRate rate = null;
                try{

                    //default Exchange rate Provider

                    if(moduleManager.getExchangeProvider()==null) {
                        List<ExchangeRateProvider> providers = new ArrayList(moduleManager.getExchangeRateProviderManagers());

                        exchangeProviderId = providers.get(0).getProviderId();
                        moduleManager.setExchangeProvider(exchangeProviderId);

                    }
                    else
                    {
                        exchangeProviderId =moduleManager.getExchangeProvider();
                    }


                    rate =  moduleManager.getCurrencyExchange(exchangeProviderId);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                return rate;
            }
        };

        fermatWorker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                if (result != null && result.length > 0) {

                    ExchangeRate rate = (ExchangeRate) result[0];
                    if(rate != null)
                    {
                        // progressBar.setVisibility(View.GONE);
                        txt_rate_amount.setText("1 BTC = " + String.valueOf(
                                WalletUtils.formatAmountStringWithDecimalEntry(
                                        rate.getPurchasePrice(),
                                        MAX_DECIMAL_FOR_RATE,
                                        MIN_DECIMAL_FOR_RATE)) + " USD");

                        //get available balance to actual exchange rate
                        actuaExchangeRate = Double.parseDouble(
                                WalletUtils.formatAmountStringWithDecimalEntry(rate.getPurchasePrice(),
                                        MAX_DECIMAL_FOR_RATE,
                                        MIN_DECIMAL_FOR_RATE));

                        appSession.setData(SessionConstant.ACTUAL_EXCHANGE_RATE, actuaExchangeRate);

                        updateBalances();

                    }
                    else {
                        ErrorExchangeRateConnectionDialog dialog_error = new ErrorExchangeRateConnectionDialog(getActivity());
                        dialog_error.show();
                    }


                }
                else {
                    ErrorExchangeRateConnectionDialog dialog_error = new ErrorExchangeRateConnectionDialog(getActivity());
                    dialog_error.show();
                    //makeText(getActivity(), "Cant't Get Exhange Rate Info, check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                //  progressBar.setVisibility(View.GONE);

                txt_rate_amount.setVisibility(View.GONE);

                ErrorManager errorManager = appSession.getErrorManager();
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
                else
                    Log.e("Exchange Rate", ex.getMessage(), ex);

                ErrorExchangeRateConnectionDialog dialog_error = new ErrorExchangeRateConnectionDialog(getActivity());
                dialog_error.show();
            }
        });

        fermatWorker.execute();
    }*/
}

