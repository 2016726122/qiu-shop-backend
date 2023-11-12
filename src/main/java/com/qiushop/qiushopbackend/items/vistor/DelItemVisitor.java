package com.qiushop.qiushopbackend.items.vistor;

import com.qiushop.qiushopbackend.items.composite.AbstractProductItem;
import com.qiushop.qiushopbackend.items.composite.ProductComposite;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelItemVisitor implements ItemVisitor<AbstractProductItem> {

    //注入 redisProcessor
    @Autowired
    private RedisCommonProcessor redisProcessor;

    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {
        //从 Redis 中获取当前缓存数据（商品类目树形结构数据）
        ProductComposite currentItem = (ProductComposite) redisProcessor.get("items");
        //需要删除的商品类目
        ProductComposite delItem = (ProductComposite) productItem;
        //不可删除根节点
        if (delItem.getId() == currentItem.getId()) {
            throw new UnsupportedOperationException("根节点不能删。");
        }
        //如果被删除节点的父节点为当前节点，则直接删除
        if (delItem.getPid() == currentItem.getId()) {
            currentItem.delProductChild(delItem);
            return currentItem;
        }
        //通过 delChild 方法进行递归寻找被删除的类目位置
        delChild(delItem, currentItem);
        return currentItem;
    }

    //进行递归寻找被删除的类目位置
    private void delChild(ProductComposite productItem, ProductComposite currentItem) {
        for (AbstractProductItem abstractItem : currentItem.getChild()) {
            ProductComposite item = (ProductComposite) abstractItem;
            if (item.getId() == productItem.getPid()) {
                item.delProductChild(productItem);
                break;
            } else {
                delChild(productItem, item);
            }
        }
    }
}
