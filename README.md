# 📦 E-Commerce API

Bu loyiha — kichik internet-do‘kon uchun backend API.  
Unda **mahsulotlar**, **buyurtmalar** va **buyurtma elementlari** bilan ishlash imkoniyati bor.  
Loyiha **Spring Boot**, **JPA/Hibernate**, **Flyway** va **H2 Database** yordamida yozilgan.

---

## 🚀 Texnologiyalar

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Flyway** – ma’lumotlar bazasi migratsiyalari uchun
- **H2 Database** – test uchun yengil DB
- **Lombok** – kodni ixcham yozish uchun
- **Jakarta Validation** – requestlarni tekshirish uchun

---

## 📂 Loyiha tuzilishi

src
├── main
│ ├── java/com/example/ecommerce
│ │ ├── controller/ → API endpoint’lar
│ │ ├── dto/ → Request/Response modellari
│ │ ├── model/ → Entity’lar
│ │ ├── repository/ → JPA repository’lar
│ │ ├── service/ → Biznes logika
│ │ └── component/ → Umumiy klasslar (BaseMapper va h.k.)
│ └── resources
│ ├── application.yml → Sozlamalar
│ └── db/migration → Flyway SQL fayllar
└── test → Unit va integration testlar

yaml
Copy
Edit

---

## 🛠 Ishga tushirish

### 1. Talablar
- **Java 17** yoki undan yuqori
- **Maven** yoki **Gradle**
- Internet (dependency’lar yuklash uchun)

### 2. Repository’ni klonlash
```bash
git clone https://github.com/username/ecommerce-api.git
cd ecommerce-api
3. Ishga tushirish
./mvnw spring-boot:run
yoki

mvn spring-boot:run
📜 API endpoint’lar
Mahsulotlar
Method	Endpoint	Tavsif
GET	/api/v1/products	Barcha mahsulotlarni olish (pagination)
GET	/api/v1/products/search	Nom yoki kategoriya bo‘yicha qidirish
GET	/api/v1/products/{id}	ID bo‘yicha mahsulot olish
POST	/api/v1/products	Yangi mahsulot qo‘shish
PUT	/api/v1/products/{id}	Mahsulotni yangilash
DELETE	/api/v1/products/{id}	Mahsulotni o‘chirish

Buyurtmalar
Method	Endpoint	Tavsif
GET	/api/v1/orders	Barcha buyurtmalar ro‘yxati
GET	/api/v1/orders/{id}	ID bo‘yicha buyurtma olish
POST	/api/v1/orders	Yangi buyurtma yaratish
PUT	/api/v1/orders/{id}/status	Buyurtma holatini yangilash
DELETE	/api/v1/orders/{id}	Buyurtmani bekor qilish
GET	/api/v1/orders/customer/{email}	Mijoz email’iga qarab buyurtmalar

🗄 Ma’lumotlar bazasi
Loyiha ishga tushganda Flyway avtomatik ravishda quyidagi jadvalarni yaratadi:

products

orders

order_items

Shuningdek, V2__insert_sample_data.sql faylida test uchun namunaviy ma’lumotlar mavjud.

H2 konsolga kirish:

http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (bo‘sh)

🧪 Testlar
Loyihada JUnit 5 va Spring Boot Test yordamida yozilgan unit va integration testlar mavjud.

Testlarni ishlatish:
    
mvn test

Qo'shimcha http testlar yozilgan
agar vaqt yetkanida. Project By Feature bi;an yaratkan bo'lar edim

