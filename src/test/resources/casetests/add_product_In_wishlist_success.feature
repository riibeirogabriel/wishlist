Feature: Add product non existent in wishlist
  Scenario: Client adds a product non existent in wishlist
    Given the client has a wishlist without a specific product
    When the client inserts this specific product in wishlist
    Then the client inserts the product in wishlist successfully