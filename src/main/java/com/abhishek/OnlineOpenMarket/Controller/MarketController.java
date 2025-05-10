package com.abhishek.OnlineOpenMarket.Controller;

import com.abhishek.OnlineOpenMarket.Data.Entities.*;
import com.abhishek.OnlineOpenMarket.Data.Model.*;
import com.abhishek.OnlineOpenMarket.Repository.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@RestController
public class MarketController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipTierRepository membershipTierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrdersDetailRepository ordersDetailRepository;

    @Value("${self.callback.url}")
    private String selfCallbackURL;

    @Value("${paymentgateway.base.url}")
    private String dummyPaymentGatewayURL;

    @Value("${deliveryservice.base.url}")
    private String dummyDeliveryServiceURL;

    Gson gson = new Gson();

    @PostMapping("/webhook")
    public String webhookCallBack(@RequestBody PayloadModel payloadModel) {
        dealWithWebhook(payloadModel);
        return "ACK";
    }

    @PostMapping("/CreateUser")
    public String createUser(@RequestBody CreateUserModel createUserModel) {
        User savedUser;
        try {
            User newUser = new User();
            if (membershipTierRepository.findById(createUserModel.getMembershipTierID()).isPresent()) {
                newUser.setFirstName(createUserModel.getFirstName());
                newUser.setLastName(createUserModel.getLastName());
                newUser.setWalletAmount(0D);
                newUser.setMembershipTierID(createUserModel.getMembershipTierID());
                newUser.setMembershipStartedFrom(Instant.now());
                newUser.setAddress(createUserModel.getAddress());
            } else throw new RuntimeException("MembershipTier not found.");
            savedUser = userRepository.save(newUser);
        } catch (Exception e) {
            return "There was an issue while saving user. Error: " + e.getMessage();
        }
        return "User was created successfully. ID of the user: " + savedUser.getUserId();
    }

    @PostMapping("/CreateMembershipTier")
    public String createMembershipTier(@RequestBody CreateMembershipTierModel createMembershipTierModel) {
        MembershipTier savedMembershipTier;
        try {
            if (membershipTierRepository.findByName(createMembershipTierModel.getName()) == null) {
                MembershipTier newMembershipTier = getMembershipTier(createMembershipTierModel);
                savedMembershipTier = membershipTierRepository.save(newMembershipTier);
            } else throw new RuntimeException("There is already a tier with the same name!");
        } catch (Exception e) {
            return "There was an error saving this membership tier. Error: " + e.getMessage();
        }
        return "Membership Tier has been saved with ID: " + savedMembershipTier.getTierID();
    }

    private static MembershipTier getMembershipTier(CreateMembershipTierModel createMembershipTierModel) {
        MembershipTier newMembershipTier = new MembershipTier();
        newMembershipTier.setName(createMembershipTierModel.getName());
        newMembershipTier.setPrice(createMembershipTierModel.getPrice());
        newMembershipTier.setOneDayDelivery(createMembershipTierModel.isOneDayDelivery());
        newMembershipTier.setFreeVideoService(createMembershipTierModel.isFreeVideoService());
        newMembershipTier.setFreeDelivery(createMembershipTierModel.isFreeDelivery());
        newMembershipTier.setFivePCCashback(createMembershipTierModel.isFivePCCashback());
        return newMembershipTier;
    }

    @PostMapping("/CreateCategory")
    public String createCategory(@RequestBody CreateCategoryModel createCategoryModel) {
        Category savedCategory;
        try {
            if (categoryRepository.findByName(createCategoryModel.getName()) == null) {
                Category newCategory = new Category();
                newCategory.setName(createCategoryModel.getName());
                newCategory.setDescription(createCategoryModel.getDescription());
                savedCategory = categoryRepository.save(newCategory);
            } else throw new RuntimeException("There is already a Category with the same name!");
        } catch (Exception e) {
            return "There was an error saving this category. Error: " + e.getMessage();
        }
        return "Category has been saved with ID: " + savedCategory.getCategoryID();
    }

    @PostMapping("/CreateSubCategory")
    public String createSubCategory(@RequestBody CreateSubCategoryModel createSubCategoryModel) {
        SubCategory savedSubCategory;
        try {
            if (subCategoryRepository.findByName(createSubCategoryModel.getName()) == null && categoryRepository.existsById(createSubCategoryModel.getCategoryID())) {
                SubCategory newSubCategory = new SubCategory();
                newSubCategory.setName(createSubCategoryModel.getName());
                newSubCategory.setDescription(createSubCategoryModel.getDescription());
                newSubCategory.setCategoryID(createSubCategoryModel.getCategoryID());
                savedSubCategory = subCategoryRepository.save(newSubCategory);
            } else throw new RuntimeException("There is already a SubCategory with the same name!");
        } catch (Exception e) {
            return "There was an error saving this SubCategory. Error: " + e.getMessage();
        }
        return "SubCategory has been saved with ID: " + savedSubCategory.getSubCategoryID();
    }

    @PostMapping("/CreateProduct")
    public String createProduct(@RequestBody CreateProductModel createProductModel) {
        Product savedProduct;
        try {
            if (subCategoryRepository.findById(createProductModel.getSubCategoryID()).isPresent()) {
                Product newProduct = new Product();
                newProduct.setName(createProductModel.getName());
                newProduct.setPrice(createProductModel.getPrice());
                newProduct.setSubCategoryID(createProductModel.getSubCategoryID());
                newProduct.setFeatures(createProductModel.getFeatures());
                savedProduct = productRepository.save(newProduct);
            } else
                throw new RuntimeException("SubCategory ID was not found.");
        } catch (Exception e) {
            return "There was an error in creating a product entry. Error: " + e.getMessage();
        }
        return "Product has been saved with ID: " + savedProduct.getProductID();
    }

    @PostMapping("/CreateOrder")
    public String createOrder(@RequestBody CreateOrderModel createOrderModel) {
        OrderDetail savedOrderDetail;
        try {
            if (userRepository.existsById(createOrderModel.getUserID())) {
                var newOrder = new OrderDetail();
                Double total = 0.0;
                newOrder.setOrderedByID(createOrderModel.getUserID());
                newOrder.setOrderType(createOrderModel.getOrderType());
                switch (createOrderModel.getOrderType()) {
                    case "ProductOrder" -> {
                        for (Long productID : createOrderModel.getProductIDs()) {
                            if (productRepository.findById(productID).isPresent()) {
                                newOrder.getProductsIDs().add(productID);
                                total += productRepository.findById(productID).get().getPrice();
                            } else
                                throw new RuntimeException("Product ID mentioned was not found: " + productID);
                        }
                    }
                    case "WalletAddition" -> {
                        total = createOrderModel.getAmount();
                    }
                    default -> {
                            throw new RuntimeException("Switch case hit a default: " + createOrderModel.getOrderType());
                    }
                }
                newOrder.setOrderAmount(total);
                savedOrderDetail = ordersDetailRepository.save(newOrder);
                var paymentRequest = new PaymentRequest();
                paymentRequest.setOrderID(savedOrderDetail.getOrderID());
                paymentRequest.setAmount(total);
                paymentRequest.setCallbackURL(selfCallbackURL + "webhook");
                var headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<>(gson.toJson(paymentRequest), headers);
                String result = new RestTemplate().postForObject(dummyPaymentGatewayURL + "CreatePayment", request, String.class);
                savedOrderDetail.setPaymentID(Long.valueOf(result));
                savedOrderDetail.setOrderStatus("Payment In Progress");
                savedOrderDetail = ordersDetailRepository.save(savedOrderDetail);
            } else
                throw new RuntimeException("User ID mentioned was not found: " + createOrderModel.getUserID());
        } catch (Exception e) {
            return "There was an error in creating an order. Error: " + e.getMessage();
        }
        return "Order was created with ID: " + savedOrderDetail.getOrderID();
    }

    private void dealWithWebhook(PayloadModel payloadModel) {
        OrderDetail order;
        try {
            if (ordersDetailRepository.existsById(payloadModel.getOrderID())) {
                order = ordersDetailRepository.findById(payloadModel.getOrderID()).get();
                switch (payloadModel.getEventType()) {
                    case "PaymentComplete" -> {
                        var paymentData = gson.fromJson(payloadModel.getPayloadData(),PaymentModel.class);
                        order.setOrderStatus("Payment is Completed");
                        ordersDetailRepository.save(order);
                        switch(order.getOrderType()){
                            case "WalletAddition" -> {
                                addFundsToWalletFinalizer(paymentData.getOrderID(),paymentData.getAmount());
                                order.setOrderStatus("Wallet Addition Order Completed");
                                ordersDetailRepository.save(order);
                            }
                            case "ProductOrder" -> {
                                order.setOrderStatus("Product Order Completed");
                                ordersDetailRepository.save(order);
                                startDeliveryProcess(order);
                            }
                            default ->
                                throw new Exception("Switch hit a default: " + order.getOrderType());
                        }
                        ordersDetailRepository.save(order);
                    }
                    case "PaymentFailed" -> {
                        order.setOrderStatus("Payment is Failed");
                        ordersDetailRepository.save(order);
                    }
                    case "DeliveryUpdate" -> {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode node = mapper.readTree(payloadModel.getPayloadData());
                        order.setDeliveryID(node.get("deliveryID").asLong());
                        order.setDeliveryStatus(node.get("deliveryStatus").asText());
                        ordersDetailRepository.save(order);
                    }
                    default ->{
                        System.out.println("Payload: " + payloadModel);
                        throw new RuntimeException("Switch hit default case, eventType received: " + payloadModel.getEventType());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred while dealing with Webhook, Error: " + e.getMessage());
        }
    }

    @PostMapping("/ChangeUserSub")
    private String changeSubscriptionTier(@RequestBody MembershipTierChangeModel membershipTierChangeModel) {
        User user;
        MembershipTier newMembershipTier;
        MembershipTier oldMembershipTier;
        try {
            if (userRepository.existsById(membershipTierChangeModel.getUserID()))
                user = userRepository.findById(membershipTierChangeModel.getUserID()).get();
            else
                throw new RuntimeException("The User with given ID was not found!");
            if (membershipTierRepository.existsById(user.getMembershipTierID()))
                oldMembershipTier = membershipTierRepository.findById(user.getMembershipTierID()).get();
            else
                throw new RuntimeException("The User's current MembershipTier ID was not found!");
            if (membershipTierRepository.existsById(membershipTierChangeModel.getMembershipTierID()))
                newMembershipTier = membershipTierRepository.findById(membershipTierChangeModel.getMembershipTierID()).get();
            else
                throw new RuntimeException("The User's new MembershipTier ID was not found!");
            if (Objects.equals(newMembershipTier.getTierID(), oldMembershipTier.getTierID()))
                throw new RuntimeException("Change cannot be performed as new and old membership tiers are the same!");
        } catch (Exception e) {
            return "There was an error in processing this request. Error: " + e.getMessage();
        }
        var daysUsed = Duration.between(Instant.now(), user.getMembershipStartedFrom()).toDays();
        var extraCost = (newMembershipTier.dailyPrice() - oldMembershipTier.dailyPrice()) * (364 - daysUsed);
        user.setWalletAmount(user.getWalletAmount() - extraCost);
        user.setMembershipTierID(newMembershipTier.getTierID());
        userRepository.save(user);
        if (extraCost > 0)
            return "Membership Tier has been upgraded to " + newMembershipTier.getName() + ". A charge of Rs." + extraCost + " has been deducted from your wallet.";
        else
            return "Membership Tier has been downgraded to " + newMembershipTier.getName() + ". A refund of Rs." + extraCost + " has been added to your wallet.";
    }

    @PostMapping("AddFundsToWallet")
    private String addFundsToWalletInitializer(@RequestBody WalletFundModel walletFundModel) {
        try {
            if(walletFundModel.getFundAmount()>0) {
                if (userRepository.existsById(walletFundModel.getUserID())) {
                    CreateOrderModel createOrderModel = new CreateOrderModel();
                    createOrderModel.setUserID(walletFundModel.getUserID());
                    createOrderModel.setOrderType("WalletAddition");
                    createOrderModel.setAmount(walletFundModel.getFundAmount());
                    System.out.println(createOrder(createOrderModel));
                } else
                    throw new RuntimeException("User not found!");
            }
            else
                throw new RuntimeException("Fund amount was negative!");
        } catch (Exception e) {
            return "There was an issue while creating a wallet change request. Error: " + e.getMessage();
        }
        return "Wallet change request has been made. Proceed to payment completion for wallet to be updated.";
    }

    private void addFundsToWalletFinalizer(Long orderID, Double amount) throws RuntimeException {
        OrderDetail orderDetail;
        User user;
        if (ordersDetailRepository.existsById(orderID)) {
            orderDetail = ordersDetailRepository.findById(orderID).get();
            if(userRepository.existsById(orderDetail.getOrderID())) {
                user = userRepository.findById(orderDetail.getOrderedByID()).get();
                user.setWalletAmount(user.getWalletAmount()+amount);
                userRepository.save(user);
            } else
                throw new RuntimeException("In addFundsToWalletFinalizer: User was not found!");
        } else
            throw new RuntimeException("In addFundsToWalletFinalizer: Order was not found!");
    }

    private void startDeliveryProcess(OrderDetail order) {
        User user;
        MembershipTier membershipTier;
        try{
            if(userRepository.existsById(order.getOrderedByID()))
                user = userRepository.findById(order.getOrderedByID()).get();
            else
                throw new RuntimeException("User with given ID was not found!");
            if(membershipTierRepository.existsById(user.getMembershipTierID()))
                membershipTier = membershipTierRepository.findById(user.getMembershipTierID()).get();
            else
                throw new RuntimeException("User with given ID was not found!");
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred while creating a delivery request. Error: " + e.getMessage());
        }
        CreateDeliveryRequestModel createDeliveryRequestModel = new CreateDeliveryRequestModel();
        createDeliveryRequestModel.setOneDayDelivery(membershipTier.isOneDayDelivery());
        createDeliveryRequestModel.setOrderID(order.getOrderID());
        createDeliveryRequestModel.setCallbackURL(selfCallbackURL + "webhook");
        createDeliveryRequestModel.setAddress(user.getAddress());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(gson.toJson(createDeliveryRequestModel), headers);
        CreateDeliveryResponseModel response = new RestTemplate().postForObject(dummyDeliveryServiceURL + "CreateDeliveryRequest", request, CreateDeliveryResponseModel.class);
        System.out.println("Delivery Service called.\nDelivery ID/Tracking ID: " + response.getDeliveryID() + ".\nResponse status: " + response.getStatus() + ".\nResponse Message: " + response.getMessage());
        order.setDeliveryID(response.getDeliveryID());
        order.setDeliveryStatus("Starting Delivery Process");
        ordersDetailRepository.save(order);
    }
}
