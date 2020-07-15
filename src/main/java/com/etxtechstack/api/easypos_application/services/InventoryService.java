package com.etxtechstack.api.easypos_application.services;

import com.etxtechstack.api.easypos_application.models.Product;
import com.etxtechstack.api.easypos_application.models.ProductCategory;
import com.etxtechstack.api.easypos_application.models.ProductStockUnit;
import com.etxtechstack.api.easypos_application.models.StockUnit;
import com.etxtechstack.api.easypos_application.repositories.ProductCategoryRepository;
import com.etxtechstack.api.easypos_application.repositories.ProductRepository;
import com.etxtechstack.api.easypos_application.repositories.ProductStockUnitRepository;
import com.etxtechstack.api.easypos_application.repositories.StockUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private StockUnitRepository stockUnitRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockUnitRepository productStockUnitRepository;

    public List<ProductCategory> getAllProductCategories() {
        try {
            return productCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ProductCategory getProductCategoryById(Integer stockUnitId) {
        try {
            return productCategoryRepository.findById(stockUnitId).orElseThrow(()-> {
                return new RuntimeException("Product Category By Id /" + stockUnitId + "/ Not Found");
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ProductCategory saveProductCategory(ProductCategory model) {
        try {
            return productCategoryRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteProductCategory(ProductCategory model) {
        try {
            productCategoryRepository.delete(model);
        } catch (Exception e) {
            throw new RuntimeException("Error! Product Category Is In Use");
        }
    }


    //================PRODUCT

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product getProductById(Integer productId) {
        try {
            return productRepository.findById(productId).orElseThrow(()-> {
                return new RuntimeException("Product Category By Id /" + productId + "/ Not Found");
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product createProduct(Product model) {
        model.setStatus("Y");
        try {
            return productRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product saveProduct(Product model) {

        try {
            return productRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteProduct(Product model) {
        try {
            productRepository.delete(model);
        } catch (Exception e) {
            throw new RuntimeException("Error! Product Is In Use");
        }
    }


    //=============stock unit===================================

    public List<StockUnit> getAllStockUnits() {
        try {
            return stockUnitRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public StockUnit getStockUnitById(Integer stockUnitId) {
        try {
            return stockUnitRepository.findById(stockUnitId).orElseThrow(()-> {
                return new RuntimeException("Stock Unit By Id /" + stockUnitId + "/ Not Found");
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public StockUnit saveStockUnit(StockUnit model) {
        try {
            return stockUnitRepository.save(model);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteStockUnit(StockUnit model) {
        try {
            stockUnitRepository.delete(model);
        } catch (Exception e) {
            throw new RuntimeException("Error! Stock Unit In Use");
        }
    }

    public void addStockUnitToProduct(ProductStockUnit model) {
        try {
            productStockUnitRepository.saveProductStockUnit(model.getProduct().getId(), model.getStockUnit().getId(),
                    model.getFactor(), model.getPrice(), model.getStatus());
        } catch (Exception e) {
            throw new RuntimeException("Error! Please Make Sure You Dont Add Duplicate Stock Units");
        }
    }

    public void updateProductStockUnit(ProductStockUnit model) {
        try {
            productStockUnitRepository.updateProductStockUnit(model.getProduct().getId(), model.getStockUnit().getId(),
                    model.getFactor(), model.getPrice(), model.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeStockUnitFromProduct(ProductStockUnit model) {
        try {
            productStockUnitRepository.deleteProductStockUnit(model.getProduct().getId(), model.getStockUnit().getId());
        } catch (Exception e) {
            throw new RuntimeException("Error! Please Make Sure Stock Unit Is Not In Use!");
        }
    }

    public ProductStockUnit getProductStockUnitByProductAndStockUnit(Product product, StockUnit stockUnit) {
        try {
            Optional<ProductStockUnit> ps = productStockUnitRepository.findProductStockUnitByProductAndStockUnit(product, stockUnit);
            if(ps.isPresent()) {
                return ps.get();
            } else {
                throw new RuntimeException("Stock Unit Not Found For Product");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<ProductStockUnit> getProductUnitStockUnits(Product product) {
        try {
            return productStockUnitRepository.getProductAllUnitStockUnit(product.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
