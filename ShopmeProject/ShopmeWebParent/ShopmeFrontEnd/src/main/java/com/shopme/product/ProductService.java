package com.shopme.product;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 9;
	public static final int SEARCH_RESULTS_PER_PAGE = 9;
	
	@Autowired private ProductRepository repo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
	public List<Product> listAll() {
		return (List<Product>) repo.findAlls();
	}
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		if (product == null) {
			throw new ProductNotFoundException("Could not find any product with alias " + alias);
		}
		
		return product;
	}
	
	public Product getProduct(Integer id) throws ProductNotFoundException {
		try {
			Product product = repo.findById(id).get();
			return product;
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}	
	
	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return repo.search(keyword, pageable);
		
	}
}
