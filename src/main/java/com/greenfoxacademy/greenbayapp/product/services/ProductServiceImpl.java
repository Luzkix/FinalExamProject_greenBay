package com.greenfoxacademy.greenbayapp.product.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.services.BidService;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.repositories.ProductRepository;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
  private ProductRepository productRepository;
  private BidService bidService;

  public ProductServiceImpl(ProductRepository productRepository, @Lazy BidService bidService) {
    this.productRepository = productRepository;
    this.bidService = bidService;
  }

  @Override
  public NewProductResponseDTO postNewProduct(NewProductRequestDTO request, UserEntity user) {
    Product product = setUpNewProduct(request, user);
    return prepareNewProductResponseDTO(product, user);
  }

  public NewProductResponseDTO prepareNewProductResponseDTO(Product product, UserEntity user) {
    NewProductResponseDTO response = new NewProductResponseDTO();
    BeanUtils.copyProperties(product,response);
    response.setSellerName(user.getUsername());
    return response;
  }

  public Product setUpNewProduct(NewProductRequestDTO request, UserEntity user) {
    Product product = new Product();
    BeanUtils.copyProperties(request,product);
    product.setSold(false);
    product.setEnlistingTime(LocalDateTime.now());
    product.setSeller(user);
    return productRepository.save(product);
  }

  @Override
  public UnsoldProductsResponseDTO filterUnsoldProducts(Integer pageId) {
    Pageable pageable = PageRequest.of(pageId - 1, 5);
    List<Product> unsoldProducts = productRepository.filterUnsoldPaginated(pageId, pageable);
    List<UnsoldProductDTO> convertedProducts = convertProductsToUnsoldProductsDetailsDTO(unsoldProducts);
    return new UnsoldProductsResponseDTO(convertedProducts);
  }

  public List<UnsoldProductDTO> convertProductsToUnsoldProductsDetailsDTO(List<Product> products) {
    List<UnsoldProductDTO> dtos = new ArrayList<>();

    for (int i = 0; i < products.size(); i++) {
      UnsoldProductDTO dto = new UnsoldProductDTO();
      BeanUtils.copyProperties(products.get(i),dto);

      Bid highestBid = products.get(i).getHighestBid();
      if (highestBid != null) dto.setHighestBid(highestBid.getBidPrice());

      dtos.add(dto);
    }
    return dtos;
  }

  @Override
  public ProductDetailsResponseDTO getProductDetails(Long id, UserEntity user)
      throws NotFoundException, AuthorizationException {
    Product product = productRepository.findById(id).orElse(null);

    if (product == null) throw new NotFoundException();
    if (!product.getSeller().getId().equals(user.getId()))
      throw new AuthorizationException("Not authorized to view details of selected item!");

    ProductDetailsResponseDTO response = convertProductIntoProductDetailsResponseDTO(product);

    return response;
  }

  public ProductDetailsResponseDTO convertProductIntoProductDetailsResponseDTO(Product product) {
    ProductDetailsResponseDTO response = new ProductDetailsResponseDTO();

    BeanUtils.copyProperties(product,response);
    response.setSellerId(product.getSeller().getId());
    response.setSellerName(product.getSeller().getUsername());

    if (product.getBuyer() != null) {
      response.setBuyerId(product.getBuyer().getId());
      response.setBuyerName(product.getBuyer().getUsername());
    }

    if (product.getBids() != null) {
      response.setBids(bidService.convertSetOfBidsIntoListOfBidDetailDTOs(product.getBids()));
    }

    return response;
  }

  @Override
  public Product findProductById(Long productId) {
    return productRepository.findById(productId).orElse(null);
  }

  @Override
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }
}
