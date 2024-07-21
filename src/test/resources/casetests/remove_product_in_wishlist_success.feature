Feature: Remove product existent in wishlist
  Scenario: Client removes a product existent in wishlist
    Given the client has a wishlist with one product
    When the client removes this specific product in wishlist
    Then the client removes this product in wishlist successfully