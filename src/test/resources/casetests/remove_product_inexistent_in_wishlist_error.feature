Feature: Remove a product not inserted in client wishlist
  Scenario: Client removes a product not inserted in its wishlist
    Given the client has a wishlist with some products
    When the client removes a product non existent in its wishlist
    Then the client receives an error message about a product not found in its wishlist