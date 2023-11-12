package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.items.composite.AbstractProductItem;
import com.qiushop.qiushopbackend.items.composite.ProductComposite;
import com.qiushop.qiushopbackend.items.vistor.AddItemVisitor;
import com.qiushop.qiushopbackend.items.vistor.DelItemVisitor;
import com.qiushop.qiushopbackend.mapper.ProductItemRepository;
import com.qiushop.qiushopbackend.pojo.ProductItem;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductItemService {

    //引入 Redis 处理类
    @Autowired
    private RedisCommonProcessor redisProcessor;
    //引入商品类目查询的持久层组件
    @Autowired
    private ProductItemRepository productItemRepository;
    //引入商品类目添加访问者
    @Autowired
    private AddItemVisitor addItemVisitor;
    //引入商品类目删除访问者
    @Autowired
    private DelItemVisitor delItemVisitor;

    //获取商品类目信息
    public ProductComposite fetchAllItems() {
        //先查询 Redis 缓存，如果不为 null，直接返回即可
        Object cacheItems = redisProcessor.get("items");
        if (cacheItems != null) {
            return (ProductComposite) cacheItems;
        }
        //如果 Redis 缓存为 null，则查询 DB，调用 findAll 方法获取所有商品类目
        List<ProductItem> fetchDbItems = productItemRepository.findAll();
        //将 DB 中的商品类目信息拼装成组合模式的树形结构
        ProductComposite items = generateProductTree(fetchDbItems);
        if (items == null) {
            throw new UnsupportedOperationException("Product items should not be empty in DB");
        }
        //将商品类目信息设置到 Redis 缓存中，下次查询可直接通过 Redis 缓存获取到
        redisProcessor.set("items",items);
        return items;
    }

    private ProductComposite generateProductTree(List<ProductItem> fetchDbItems) {
        List<ProductComposite> composites = new ArrayList<>(fetchDbItems.size());
        fetchDbItems.forEach(dbItem -> composites.add(ProductComposite.builder()
                .id(dbItem.getId())
                .name(dbItem.getName())
                .pid(dbItem.getPid())
                .build()));
        Map<Integer, List<ProductComposite>> groupingList = composites.stream().collect(Collectors.groupingBy(ProductComposite::getPid));
        composites.stream().forEach(item -> {
            List<ProductComposite> list = groupingList.get(item.getId());
            item.setChild(list == null ? new ArrayList<>() : list.stream().map(x -> (AbstractProductItem)x).collect(Collectors.toList()));
        });
        ProductComposite composite = composites.size() == 0 ? null : composites.get(0);
        return composite;
    }

    //添加商品类目
    public ProductComposite addItems(ProductItem item) {
        //先更新数据库
        productItemRepository.addItem(item.getName(),item.getPid());
        //通过访问者模式访问树形数据结构，并添加新商品类目
        ProductComposite addItem = ProductComposite.builder()
                .id(productItemRepository.findByNameAndPid(item.getName(),item.getPid()).getId())
                .name(item.getName())
                .pid(item.getPid())
                .child(new ArrayList<>())
                .build();
        AbstractProductItem updatedItems = addItemVisitor.visitor(addItem);
        //再更新 Redis 缓存，此处可以做重试机制，如果重试不成功，可人工介入
        redisProcessor.set("items", updatedItems);
        return (ProductComposite) updatedItems;
    }

    //删除商品类目
    public ProductComposite delItems(ProductItem item) {
        //先更新数据库 只删除两级深度 可改为无限深度
        productItemRepository.delItem(item.getId());
        //通过访问者模式访问树形数据结构，并删除商品类目
        ProductComposite delItem = ProductComposite.builder()
                .id(item.getId())
                .name(item.getName())
                .pid(item.getPid())
                .build();
        AbstractProductItem updatedItems = delItemVisitor.visitor(delItem);
        //再更新 Redis 缓存，此处可以做重试机制，如果重试不成功，可人工介入
        redisProcessor.set("items", updatedItems);
        return (ProductComposite) updatedItems;
    }
}
