package com.lee.repository.network;

import com.lee.repository.javabean.FoodsBean;
import com.lee.repository.javabean.Rows;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

public class PageDataSourceFactory extends DataSource.Factory {
    public PositionalDataSource<FoodsBean> mPositionDataSource;
    public PageDataSourceFactory(PositionalDataSource<FoodsBean> rowsPositionalDataSource)
    {
        this.mPositionDataSource=rowsPositionalDataSource;
    }

    @NonNull
    @Override
    public DataSource create() {
        return mPositionDataSource;
    }
}
