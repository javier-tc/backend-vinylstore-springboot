# VinylStore API - Microservicios

API REST desarrollada con Spring Boot para Vinylstore, implementando una arquitectura de microservicios.

## Arquitectura

La aplicación está organizada en tres microservicios principales:

1. **Auth Service** - Gestión de usuarios, autenticación y autorización
2. **Product Service** - Gestión del catálogo de vinilos
3. **Cart Service** - Gestión del carrito de compras

## Tecnologías

- **Spring Boot 3.2.0**
- **Spring Security** - Autenticación y autorización
- **JWT** - Tokens de autenticación
- **Spring Data JPA** - Acceso a datos
- **MySQL** - Base de datos
- **Gradle** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate

## Requisitos Previos

- Java 17 o superior
- MySQL 8.0 o superior
- Gradle 8.14 o superior

## Configuración

### 1. Base de Datos

Crea una base de datos MySQL llamada `vinylstore_db`:

```sql
CREATE DATABASE vinylstore_db;
```

O la aplicación la creará automáticamente si tienes permisos.

### 2. Configuración de la Aplicación

Edita el archivo `src/main/resources/application.properties` y ajusta las credenciales de MySQL:

```properties
spring.datasource.username=root
spring.datasource.password=tu_password
```

### 3. Compilar y Ejecutar

```bash
# Compilar el proyecto
./gradlew build

# Ejecutar la aplicación
./gradlew bootRun
```

La aplicación estará disponible en `http://localhost:8080`

## Datos Iniciales

Al iniciar la aplicación, se cargan automáticamente:

### Usuario Administrador
- **Email**: `admin@vinylstore.com`
- **Password**: `admin123`
- **Nombre**: Administrador VinylStore
- **Rol**: ADMIN

### Productos Iniciales (5 vinilos)
1. **Abbey Road** - The Beatles (Rock, $45.000, stock: 10)
2. **The Dark Side of the Moon** - Pink Floyd (Progressive Rock, $52.000, stock: 8)
3. **Kind of Blue** - Miles Davis (Jazz, $48.000, stock: 5)
4. **Led Zeppelin IV** - Led Zeppelin (Rock, $47.000, stock: 12)
5. **Miles Davis Quintet** - Miles Davis (Jazz, $55.000, stock: 7)

## Endpoints

### Auth Service

#### `POST /api/auth/register` - Registrar nuevo usuario

**Público** - No requiere autenticación

**Request:**
```json
POST /api/auth/register
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez@example.com",
  "userId": 2,
  "role": "USER"
}
```

---

#### `POST /api/auth/login` - Iniciar sesión

**Público** - No requiere autenticación

**Request:**
```json
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@vinylstore.com",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "admin@vinylstore.com",
  "userId": 1,
  "role": "ADMIN"
}
```

---

#### `POST /api/auth/logout` - Cerrar sesión

**Autenticado** - Requiere token JWT

**Request:**
```
POST /api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```
(No content)
```

---

#### `GET /api/auth/profile/{userId}` - Obtener perfil de usuario

**Autenticado** - Requiere token JWT

**Request:**
```
GET /api/auth/profile/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "id": 1,
  "email": "admin@vinylstore.com",
  "firstName": "Administrador",
  "lastName": "VinylStore",
  "role": "ADMIN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

#### `PUT /api/auth/profile/{userId}` - Actualizar perfil de usuario

**Autenticado** - Requiere token JWT

**Request:**
```json
PUT /api/auth/profile/2
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "email": "juan.perez@example.com",
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "role": "USER",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

---

#### `GET /api/auth/users` - Listar usuarios

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```
GET /api/auth/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "email": "admin@vinylstore.com",
    "firstName": "Administrador",
    "lastName": "VinylStore",
    "role": "ADMIN",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "email": "juan.perez@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "role": "USER",
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

---

#### `PUT /api/auth/users/{userId}/role` - Cambiar rol de usuario

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```json
PUT /api/auth/users/2/role
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "email": "juan.perez@example.com",
  "firstName": "Juan",
  "lastName": "Pérez",
  "role": "ADMIN",
  "createdAt": "2024-01-15T11:00:00",
  "updatedAt": "2024-01-15T12:00:00"
}
```

---

### Product Service

#### `GET /api/products` - Listar todos los productos

**Público** - No requiere autenticación

**Request:**
```
GET /api/products
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Abbey Road",
    "artist": "The Beatles",
    "genre": "Rock",
    "description": "Álbum icónico de 1969",
    "price": 45000.00,
    "stock": 10,
    "imageUrl": "https://...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "title": "The Dark Side of the Moon",
    "artist": "Pink Floyd",
    "genre": "Progressive Rock",
    "description": "Grabación revolucionaria de rock progresivo",
    "price": 52000.00,
    "stock": 8,
    "imageUrl": "https://...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

#### `GET /api/products?genero={genero}` - Filtrar por género

**Público** - No requiere autenticación

**Request:**
```
GET /api/products?genero=Jazz
```

**Response (200 OK):**
```json
[
  {
    "id": 3,
    "title": "Kind of Blue",
    "artist": "Miles Davis",
    "genre": "Jazz",
    "description": "Obra maestra del jazz modal",
    "price": 48000.00,
    "stock": 5,
    "imageUrl": "https://...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 5,
    "title": "Miles Davis Quintet",
    "artist": "Miles Davis",
    "genre": "Jazz",
    "description": "Jazz legendario",
    "price": 55000.00,
    "stock": 7,
    "imageUrl": "https://...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

#### `GET /api/products/{id}` - Obtener producto por ID

**Público** - No requiere autenticación

**Request:**
```
GET /api/products/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Abbey Road",
  "artist": "The Beatles",
  "genre": "Rock",
  "description": "Álbum icónico de 1969",
  "price": 45000.00,
  "stock": 10,
  "imageUrl": "https://...",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

#### `GET /api/products/search?q={query}` - Buscar productos

**Público** - No requiere autenticación

**Request:**
```
GET /api/products/search?q=Beatles
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Abbey Road",
    "artist": "The Beatles",
    "genre": "Rock",
    "description": "Álbum icónico de 1969",
    "price": 45000.00,
    "stock": 10,
    "imageUrl": "https://...",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

#### `POST /api/products` - Crear producto

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```json
POST /api/products
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Thriller",
  "artist": "Michael Jackson",
  "genre": "Pop",
  "description": "Álbum más vendido de todos los tiempos",
  "price": 50000.00,
  "stock": 15,
  "imageUrl": "https://example.com/thriller.jpg"
}
```

**Response (201 Created):**
```json
{
  "id": 6,
  "title": "Thriller",
  "artist": "Michael Jackson",
  "genre": "Pop",
  "description": "Álbum más vendido de todos los tiempos",
  "price": 50000.00,
  "stock": 15,
  "imageUrl": "https://example.com/thriller.jpg",
  "createdAt": "2024-01-15T12:00:00",
  "updatedAt": "2024-01-15T12:00:00"
}
```

---

#### `PUT /api/products/{id}` - Actualizar producto

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```json
PUT /api/products/6
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Thriller",
  "artist": "Michael Jackson",
  "genre": "Pop",
  "description": "Álbum más vendido de todos los tiempos - Edición especial",
  "price": 55000.00,
  "stock": 15,
  "imageUrl": "https://example.com/thriller-special.jpg"
}
```

**Response (200 OK):**
```json
{
  "id": 6,
  "title": "Thriller",
  "artist": "Michael Jackson",
  "genre": "Pop",
  "description": "Álbum más vendido de todos los tiempos - Edición especial",
  "price": 55000.00,
  "stock": 15,
  "imageUrl": "https://example.com/thriller-special.jpg",
  "createdAt": "2024-01-15T12:00:00",
  "updatedAt": "2024-01-15T12:30:00"
}
```

---

#### `DELETE /api/products/{id}` - Eliminar producto

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```
DELETE /api/products/6
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (204 No Content):**
```
(No content)
```

---

#### `PUT /api/products/{id}/stock` - Actualizar stock

**Solo Admin** - Requiere token JWT con rol ADMIN

**Request:**
```json
PUT /api/products/1/stock
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "stock": 20
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Abbey Road",
  "artist": "The Beatles",
  "genre": "Rock",
  "description": "Álbum icónico de 1969",
  "price": 45000.00,
  "stock": 20,
  "imageUrl": "https://...",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T13:00:00"
}
```

---

### Cart Service

#### `GET /api/cart/{userId}` - Obtener carrito del usuario

**Autenticado** - Requiere token JWT

**Request:**
```
GET /api/cart/2
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "userId": 2,
    "productId": 1,
    "quantity": 2,
    "unitPrice": 45000.00,
    "subtotal": 90000.00,
    "createdAt": "2024-01-15T14:00:00",
    "updatedAt": "2024-01-15T14:00:00"
  },
  {
    "id": 2,
    "userId": 2,
    "productId": 3,
    "quantity": 1,
    "unitPrice": 48000.00,
    "subtotal": 48000.00,
    "createdAt": "2024-01-15T14:05:00",
    "updatedAt": "2024-01-15T14:05:00"
  }
]
```

---

#### `POST /api/cart/{userId}/items` - Agregar item al carrito

**Autenticado** - Requiere token JWT

**Request:**
```json
POST /api/cart/2/items
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "userId": 2,
  "productId": 1,
  "quantity": 2,
  "unitPrice": 45000.00,
  "subtotal": 90000.00,
  "createdAt": "2024-01-15T14:00:00",
  "updatedAt": "2024-01-15T14:00:00"
}
```

---

#### `PUT /api/cart/{userId}/items/{itemId}` - Actualizar cantidad

**Autenticado** - Requiere token JWT

**Request:**
```json
PUT /api/cart/2/items/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "quantity": 3
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "userId": 2,
  "productId": 1,
  "quantity": 3,
  "unitPrice": 45000.00,
  "subtotal": 135000.00,
  "createdAt": "2024-01-15T14:00:00",
  "updatedAt": "2024-01-15T14:15:00"
}
```

---

#### `DELETE /api/cart/{userId}/items/{itemId}` - Eliminar item del carrito

**Autenticado** - Requiere token JWT

**Request:**
```
DELETE /api/cart/2/items/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (204 No Content):**
```
(No content)
```

---

#### `DELETE /api/cart/{userId}` - Vaciar carrito completo

**Autenticado** - Requiere token JWT

**Request:**
```
DELETE /api/cart/2
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (204 No Content):**
```
(No content)
```

---

#### `GET /api/cart/{userId}/total` - Obtener total del carrito

**Autenticado** - Requiere token JWT

**Request:**
```
GET /api/cart/2/total
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response (200 OK):**
```json
{
  "userId": 2,
  "items": [
    {
      "id": 1,
      "userId": 2,
      "productId": 1,
      "quantity": 2,
      "unitPrice": 45000.00,
      "subtotal": 90000.00,
      "createdAt": "2024-01-15T14:00:00",
      "updatedAt": "2024-01-15T14:00:00"
    },
    {
      "id": 2,
      "userId": 2,
      "productId": 3,
      "quantity": 1,
      "unitPrice": 48000.00,
      "subtotal": 48000.00,
      "createdAt": "2024-01-15T14:05:00",
      "updatedAt": "2024-01-15T14:05:00"
    }
  ],
  "total": 138000.00,
  "totalItems": 3
}
```

## Autenticación

La API utiliza JWT (JSON Web Tokens) para la autenticación. Para acceder a endpoints protegidos, incluye el token en el header:

```
Authorization: Bearer <tu_token>
```

**Nota:** El token se obtiene al realizar login o registro exitoso. El token tiene una validez de 24 horas (86400000 ms).

## Roles

- **USER**: Usuario estándar, puede gestionar su perfil y carrito
- **ADMIN**: Administrador, acceso completo a todos los endpoints

## Estructura del Proyecto

```
src/main/java/com/vinylstore/
├── auth/              # Auth Service
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   ├── service/
│   ├── util/
│   └── filter/
├── product/           # Product Service
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── cart/              # Cart Service
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
└── config/            # Configuraciones globales
```

## Base de Datos

Las tablas se crean automáticamente mediante JPA con `spring.jpa.hibernate.ddl-auto=update`:

- **users**: Usuarios del sistema
- **products**: Catálogo de vinilos
- **cart_items**: Items del carrito de compras

## Notas

- El secreto JWT está configurado en `application.properties`. En producción, usa una clave segura.
- La aplicación crea automáticamente las tablas en la base de datos si no existen.
- Los endpoints de productos son públicos para lectura, pero requieren autenticación de admin para modificación.
- Los datos iniciales (usuario admin y productos) se cargan automáticamente al iniciar la aplicación si no existen.
- El usuario administrador por defecto es: `admin@vinylstore.com` / `admin123`

