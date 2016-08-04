## event-driven-orders

# Modules

- client-api - REST API for viewing products and placing orders
- order-service - service that handles order placement
- product-service - service that handles products
- common - defines domain entities and messages passed across services

# Order placement
- client views available products in API.
- client chooses `Products`, then places an `Order`.
- when placing an order, client-api calls order-service, which in turn contacts product-service to update `Product` counts.
- product-service responses to order-service if it succeeded or failed.
- order-service responds to API whether order was successfully placed or failed.