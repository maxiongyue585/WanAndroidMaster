package cn.white.ymc.wanandroidmaster.ui.home;

import java.util.List;
import cn.white.ymc.wanandroidmaster.base.contract.BasePresenter;
import cn.white.ymc.wanandroidmaster.data.BaseResp;
import cn.white.ymc.wanandroidmaster.data.bean.BenarBean;
import cn.white.ymc.wanandroidmaster.data.bean.HomePageArticleBean;
import cn.white.ymc.wanandroidmaster.data.bean.UserInfo;
import cn.white.ymc.wanandroidmaster.model.ApiService;
import cn.white.ymc.wanandroidmaster.model.ApiStore;
import cn.white.ymc.wanandroidmaster.util.ConstantUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页 presenter 层
 *
 * @packageName: cn.white.ymc.wanandroidmaster.ui.home
 * @fileName: HomePagePresenter
 * @date: 2018/8/6  16:41
 * @author: ymc
 * @QQ:745612618
 */

public class HomePagePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Per {

    private HomeContract.View view;
    private boolean isRefresh = true;
    private int currentPage;

    public HomePagePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void attachView(HomeContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void autoRefresh() {
        isRefresh = true;
        currentPage = 0;
        getBanner();
        getHomepageListData(currentPage);
    }

    @Override
    public void loadMore() {
        isRefresh = false;
        currentPage++;
        getHomepageListData(currentPage);
    }

    /**
     * 获取 banner 信息
     */
    @Override
    public void getBanner() {
        ApiStore.createApi(ApiService.class)
                .getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResp<List<BenarBean>>>() {
                    @Override
                    public void onError(Throwable e) {
                        view.getBannerErr(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResp<List<BenarBean>> listBaseResp) {
                        if(listBaseResp.getErrorCode() == ConstantUtil.REQUEST_ERROR){
                            view.getBannerErr(listBaseResp.getErrorMsg());
                        }else if(listBaseResp.getErrorCode() == ConstantUtil.REQUEST_SUCCESS){
                            view.getBannerOk(listBaseResp.getData());
                        }
                    }
                });
    }

    /**
     * 获取首页 信息
     * @param page
     */
    @Override
    public void getHomepageListData(int page) {
        ApiStore.createApi(ApiService.class)
                .getArticleList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResp<HomePageArticleBean>>() {
                    @Override
                    public void onError(Throwable e) {
                        view.getHomepageListErr(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResp<HomePageArticleBean> homePageArticleBeanBaseResp) {
                        if(homePageArticleBeanBaseResp.getErrorCode() == ConstantUtil.REQUEST_ERROR){
                            view.getHomepageListErr(homePageArticleBeanBaseResp.getErrorMsg());
                        }else if(homePageArticleBeanBaseResp.getErrorCode() == ConstantUtil.REQUEST_SUCCESS){
                            view.getHomepageListOk(homePageArticleBeanBaseResp.getData(),isRefresh);
                        }
                    }
                });
    }

    @Override
    public void loginUser(String username, String password) {
        ApiStore.createApi(ApiService.class)
                .login(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResp<UserInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage() != null) {
                            view.loginErr(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResp<UserInfo> baseResp) {
                        if (baseResp.getErrorCode() == ConstantUtil.REQUEST_SUCCESS) {
                            view.loginOk(baseResp.getData());
                        } else if (baseResp.getErrorCode() == ConstantUtil.REQUEST_ERROR) {
                            view.loginErr(baseResp.getErrorMsg());
                        }
                    }
                });

    }
}
