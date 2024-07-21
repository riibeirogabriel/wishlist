Feature: Add product already existent in wishlist
  Scenario: Client adds a product already existent in wishlist
    Given the client has a wishlist with a specific product
    When the client inserts the same product in wishlist
    Then the client receives an error message about wishlist already containing the product