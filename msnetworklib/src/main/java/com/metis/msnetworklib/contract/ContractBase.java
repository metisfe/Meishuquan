package com.metis.msnetworklib.contract;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 3/15/2015.
 */

public class ContractBase<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static <T> ContractBase<T> CreateContractBase(List<T> datas)
    {
        ContractBase<T> contractBaseObj = new ContractBase<T>();
        contractBaseObj.items = new ArrayList<T>(datas);
        return contractBaseObj;
    }

    protected Pagination pagination;
    protected int previousSize;
    protected ArrayList<T> items;
    protected ArrayList<T> newAddedItems;
    protected Object synchronizeObject = new Object();

    public int getCurrentSize()
    {
        return this.items.size();
    }

    public int getPreviousSize()
    {
        return this.previousSize;
    }

    public boolean hasMore()
    {
        return this.pagination != null && this.pagination.HasMore;
    }

    public Pagination getPagination()
    {
        return this.pagination;
    }

    public void commitLoadMore()
    {
        synchronized (synchronizeObject)
        {
            if (this.newAddedItems != null && this.newAddedItems.size() > 0)
            {
                this.items.addAll(this.newAddedItems);
                this.newAddedItems.clear();
            }
        }
    }

    public ContractBase<T> loadMore(JSONObject jsonObject)
    {
        return this;
    }

    public void clear()
    {
        synchronized (synchronizeObject)
        {
            this.items.clear();
        }
    }

    public void add(int index, T data)
    {
        synchronized (synchronizeObject)
        {
            if (this.items == null || index < 0 || index > this.items.size())
            {
                return;
            }

            this.items.add(index, data);
        }
    }

    public T remove(int index)
    {
        synchronized (synchronizeObject)
        {
            if (this.items == null || index < 0 || index >= this.items.size())
            {
                return null;
            }

            return this.items.remove(index);
        }
    }

    public T getItem(int index)
    {
        synchronized (synchronizeObject)
        {
            if (this.items == null || index < 0 || index >= this.items.size())
            {
                return null;
            }

            return this.items.get(index);
        }
    }

    public int calculateDifferentItemCount(ContractBase<T> target)
    {
        if (target == null || target.getCurrentSize() == 0)
        {
            return this.getCurrentSize();
        }

        int differentItemCount = 0;
        synchronized (synchronizeObject)
        {
            for (int i = 0, j = this.items.size(); i < j; i++)
            {
                T myItem = this.items.get(i);
                boolean isSame = false;
                for (int k = 0, l = target.items.size(); k < l; k++)
                {
                    T targetItem = target.items.get(k);
                    if (targetItem != null && targetItem.equals(myItem))
                    {
                        isSame = true;
                        break;
                    }
                }

                if (!isSame)
                {
                    differentItemCount++;
                }
            }
        }

        return differentItemCount;
    }

    protected void init(JSONObject jsonObject, String itemListKey, ContractUtility.ArrayItemParser<T> parser)
    {
        init(jsonObject, itemListKey, Pagination.DefaultPaginationObjectKey, parser);
    }

    protected void init(JSONObject jsonObject, String itemListKey, String paginationObjectKey, ContractUtility.ArrayItemParser<T> parser)
    {
        this.previousSize = 0;
        this.items = new ArrayList<T>();
        this.pagination = Pagination.parse(jsonObject, paginationObjectKey);

        List<T> tempList = ContractUtility.getArrayItems(jsonObject, itemListKey, parser);
        if (tempList != null)
        {
            synchronized (synchronizeObject)
            {
                this.items.addAll(tempList);
            }
        }
    }

    protected void update(JSONObject jsonObject, String itemListKey, ContractUtility.ArrayItemParser<T> parser)
    {
        update(jsonObject, itemListKey, Pagination.DefaultPaginationObjectKey, parser);
    }

    protected void update(JSONObject jsonObject, String itemListKey, String paginationObjectKey, ContractUtility.ArrayItemParser<T> parser)
    {
        this.previousSize = this.items.size();
        this.pagination = Pagination.parse(jsonObject, paginationObjectKey);

        List<T> tempList = ContractUtility.getArrayItems(jsonObject, itemListKey, parser);
        if (tempList != null)
        {
            synchronized (synchronizeObject)
            {
                if (this.newAddedItems == null)
                {
                    this.newAddedItems = new ArrayList<T>(tempList.size());
                }

                this.newAddedItems.addAll(tempList);
            }
        }
    }
}