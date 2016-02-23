package com.weiqianghu.usedbook.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.weiqianghu.usedbook.R;
import com.weiqianghu.usedbook.model.entity.BookBean;
import com.weiqianghu.usedbook.presenter.BooksPresenter;
import com.weiqianghu.usedbook.presenter.adapter.CommonAdapter;
import com.weiqianghu.usedbook.util.FragmentUtil;
import com.weiqianghu.usedbook.view.ViewHolder;
import com.weiqianghu.usedbook.view.common.BaseFragment;
import com.weiqianghu.usedbook.view.view.IBooksView;

import java.util.List;


public class MainFragment extends BaseFragment implements IBooksView {

    private GridView mBookGridView;
    private FragmentManager fragmentManager;
    private TextView mSearchEditText;
    private CommonAdapter<BookBean> booksAdapter;

    private BooksPresenter mBoksPresenter;

    private List<BookBean> books;

    private View loadingView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView(savedInstanceState);


    }

    protected void initView(Bundle savedInstanceState) {
        mBookGridView = (GridView) mRootView.findViewById(R.id.gv_book);

        //TODO 分页显示
        loadingView = getLayoutInflater(savedInstanceState).inflate(R.layout.loading, null);


        mSearchEditText = (TextView) mRootView.findViewById(R.id.et_search);
        mSearchEditText.setFocusable(false);
        mSearchEditText.setOnClickListener(new ClickListener());

        mBoksPresenter = new BooksPresenter(this);
        mBoksPresenter.loadBooks();
    }


    void gotoBookDetail(int position) {

        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        Fragment fragment = fragmentManager.findFragmentByTag(BookDetailFragment.TAG);
        if (fragment == null) {
            fragment = BookDetailFragment.getInstance();

            Bundle args = new Bundle();
            args.putSerializable("books", books.get(position));
            fragment.setArguments(args);
        } else {
            Bundle args = fragment.getArguments();
            args.putSerializable("books", books.get(position));
        }

        Fragment form = fragmentManager.findFragmentByTag(MainLayoutFragment.TAG);

        FragmentUtil.switchContentAddToBackStack(form, fragment, R.id.main_container, fragmentManager,BookDetailFragment.TAG);
    }

    @Override
    public void showBooks(List<BookBean> books) {
        this.books = books;
        booksAdapter = new CommonAdapter<BookBean>(getActivity(), books, R.layout.book_item) {
            @Override
            public void convert(ViewHolder helper, BookBean item) {
                helper.setText(R.id.tv_item_book_price, item.getPrice() + "￥");
            }
        };

        mBookGridView.setAdapter(booksAdapter);

        mBookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoBookDetail(position);
            }
        });
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.et_search:
                    gotoSearch();
            }

        }
    }

    void gotoSearch() {
        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        Fragment fragment = fragmentManager.findFragmentByTag(SearchFragment.TAG);
        if (fragment == null) {
            fragment = SearchFragment.getInstance();
        }

        Fragment from=fragmentManager.findFragmentByTag(MainLayoutFragment.TAG);
        FragmentUtil.switchContentAddToBackStack(from,fragment,R.id.main_container,fragmentManager,SearchFragment.TAG);
    }

}
