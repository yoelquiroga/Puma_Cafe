# PUMA CAFÉ — Sistema de Comercio Electrónico

## 📋 Descripción
Plataforma web para cafetería con catálogo de productos, carrito de compras, gestión de pedidos y panel administrativo. Los clientes pueden navegar, realizar pedidos y consultar su historial. Los administradores gestionan productos, pedidos, clientes y visualizan métricas del negocio.

## ✨ Funcionalidades

- **Catálogo de productos** con categorías (café preparado, grano, molido) y filtros
- **Carrito de compras persistente** en navegador (localStorage) sin recargar
- **Registro y autenticación** de usuarios con contraseñas hasheadas (BCrypt)
- **Gestión de pedidos** con trazabilidad de estados (pendiente, en ruta, entregado, cancelado)
- **Boleta de venta en PDF** profesional con logo, IGV y detalle de productos
- **Dashboard administrativo** con KPIs, gráfico de ventas mensuales y tabla de pedidos recientes
- **Formulario de contacto** con almacenamiento y vista administrativa
- **Notificaciones toast** y confirmaciones con SweetAlert2

## 🛠 Stack Tecnológico

### Backend
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.3.4
- **ORM:** Spring Data JPA, Hibernate
- **Seguridad:** spring-security-crypto (BCryptPasswordEncoder)
- **Generación PDF:** OpenPDF 1.3.30
- **Build:** Maven

### Frontend
- **Templating:** Thymeleaf
- **UI Framework:** Bootstrap 4/5
- **JavaScript:** jQuery 3, JavaScript vanilla
- **Librerías:** Chart.js, SweetAlert2, DataTables

### Base de Datos
- **Relacional:** MySQL 8

## 📁 Estructura del Proyecto

```
PumaCafe/
├── pom.xml                          # Dependencias Maven
├── src/main/java/com/springboot/
│   ├── PumaCafeJpaApplication.java  # Punto de entrada
│   ├── ResourceWebConfiguration.java
│   ├── PasswordMigrationComponent.java
│   ├── controllers/                 # Controladores MVC
│   ├── models/
│   │   ├── entiys/                  # Entidades JPA
│   │   ├── repository/              # Repositorios JPA
│   │   ├── service/                 # Interfaces de servicio
│   │   └── serviceImplements/       # Implementaciones de servicio
├── src/main/resources/
│   ├── application.properties       # Configuración de la aplicación
│   ├── static/
│   │   ├── css/                     # Hojas de estilo
│   │   └── js/                      # Scripts JavaScript
│   └── templates/
│       ├── administrador/           # Vistas del panel admin
│       └── usuario/                 # Vistas del cliente
├── imagesProducts/                  # Imágenes de productos
└── PORTFOLIO_README.md              # Documentación de portafolio
```

## 🚀 Requisitos Previos

- **JDK 17** o superior
- **Maven 3.8+**
- **MySQL 8**
- **IDE:** Eclipse (recomendado) o IntelliJ IDEA

## 🔧 Configuración y Ejecución

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd PumaCafe
```

### 2. Configurar base de datos
Crear una base de datos MySQL llamada `puma_cafe` y ejecutar el script SQL ubicado en `BD_PUMA_CAFE.txt` para crear las tablas y datos iniciales.

### 3. Configurar conexión a BD
Editar `src/main/resources/application.properties` con tus credenciales:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/puma_cafe
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 4. Ejecutar en Eclipse
1. Importar el proyecto: `File → Import → Maven → Existing Maven Projects`
2. Seleccionar la carpeta `PumaCafe` y hacer clic en `Finish`
3. Esperar a que Maven descargue las dependencias
4. Abrir `src/main/java/com/springboot/PumaCafeJpaApplication.java`
5. Click derecho → `Run As → Java Application`
6. La aplicación iniciará en `http://localhost:8080`

### 5. Credenciales de acceso
- **Administrador:** Usuario registrado en BD con perfil `ADMIN`
- **Cliente:** Registrarse desde la página de inicio de sesión


