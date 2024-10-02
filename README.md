MARKET PLACE CLOTHING API

This API is designed to support a marketplace for clothing. It provides functionality for customers 
to browse products and for merchants to manage their products and upload posters. The API also handles product image conversions and storage.

- Customer:
  - View all products available in the marketplace.
  - View products by merchant.
  
- Merchant:
  - Create, update, and delete products.
  - Upload and manage posters for products.

- Poster Management:
  - Upload product posters in various formats (including `.webp`).
  - Convert and resize images automatically.
  - Retrieve posters via the API.


Tech Stack

- Backend: Spring Boot
- Database: PostgreSQL
- Security: JWT for authentication and role-based access
- File Handling: ImageIO, Thumbnails, MultipartFile

HOW TO USE THIS API
1. Clone this Repo
2. then open it in your favourite IDE, i use Intellij Idea
3. This API use Midtrans as payment Gateway, so make sure you have server key as your env variable,
4. then don't forget to set up your PostgreSQL in app.properties
5. Run this API
6. use Postman to test this API, provided in this repo,
