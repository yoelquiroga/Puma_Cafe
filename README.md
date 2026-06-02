# PUMA CAFÉ

## 📋 Descripción

Sistema de comercio electrónico construido para cafeterías que aún operan con ventas presenciales como único canal, donde la pérdida de clientes digitales, la gestión manual de pedidos y la falta de trazabilidad eran el problema del día a día. Se implementó un carrito de compras persistente con notificaciones toast, autenticación segura con BCrypt con migración automática de credenciales legacy, generación programática de boletas PDF con OpenPDF incluyendo desglose de IGV, y un dashboard administrativo con Chart.js para visualización de ventas mensuales y KPIs en tiempo real. El resultado es un canal de ventas digital funcional que permite a los clientes navegar el catálogo, realizar pedidos con seguimiento de estado y recibir su boleta digital, mientras los administradores gestionan productos, pedidos y contactos desde un panel centralizado con métricas del negocio.

## ✨ Funcionalidades

- **Autenticación con BCrypt y sesión server-side** — Login unificado para clientes y administradores con migración automática de contraseñas legacy a BCrypt mediante CommandLineRunner.
- **Carrito de compras persistente** — Gestión de productos, cantidades y totales sincronizada entre páginas sin llamadas al servidor, con notificaciones toast por cada acción.
- **Dashboard administrativo con métricas** — KPIs de ventas totales (excluyendo cancelados), pedidos activos, conteo de usuarios y productos con stock bajo, más gráfico de ventas mensuales con Chart.js.
- **Boleta de venta en PDF profesional** — Documento generado con OpenPDF que incluye logo corporativo, datos del cliente, tabla de productos con zebra striping, desglose de IGV 18% y estado del pedido.
- **Gestión de pedidos con trazabilidad de estados** — CRUD completo con cuatro estados (pendiente, en ruta, entregado, cancelado) y actualización desde el panel administrativo.
- **Catálogo de productos con carga de imágenes** — CRUD de productos categorizados (café preparado, grano, molido) con subida de imágenes al sistema de archivos local.
- **Formulario de contacto con persistencia** — Mensajes de clientes almacenados en base de datos con interfaz administrativa para visualización y eliminación.

## 🛠 Stack Tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Java 17, Spring Boot 3.3.4 |
| ORM / Data | Spring Data JPA, Hibernate |
| Seguridad | spring-security-crypto (BCryptPasswordEncoder) |
| PDF | OpenPDF 1.3.30 (LibrePDF) |
| Frontend Templating | Thymeleaf |
| Frontend UI | Bootstrap 4/5 |
| Librerías JS | jQuery 3, Chart.js, SweetAlert2, DataTables |
| Base de Datos | MySQL 8 |
| Build | Maven 3.8+ |
| Testing | JUnit 5, Spring Boot Test |

## 📁 Estructura del Proyecto

```
PumaCafe/
├── pom.xml                          # Dependencias Maven
├── src/main/java/com/springboot/
│   ├── PumaCafeJpaApplication.java  # Punto de entrada de la aplicación
│   ├── ResourceWebConfiguration.java
│   ├── PasswordMigrationComponent.java
│   ├── controllers/                 # Controladores MVC (8 clases)
│   ├── models/
│   │   ├── entiys/                  # Entidades JPA (6 clases)
│   │   ├── repository/              # Repositorios Spring Data JPA (5 interfaces)
│   │   ├── service/                 # Interfaces de servicio (6 interfaces)
│   │   └── serviceImplements/       # Implementaciones de servicio (6 clases)
├── src/main/resources/
│   ├── application.properties.example
│   ├── static/
│   │   ├── css/                     # Hojas de estilo (6 archivos)
│   │   ├── js/                      # Scripts JavaScript (6 archivos)
│   │   └── img/                     # Imágenes de banners y slider
│   └── templates/
│       ├── administrador/           # Plantillas del panel admin (9 archivos)
│       └── usuario/                 # Plantillas del cliente (10 archivos)
└── imagesProducts/                  # Imágenes subidas de productos
```

## 🏗 Arquitectura

```
Cliente (Browser) → Thymeleaf Templates → Spring MVC Controllers
                                                    ↓
                                          Service Layer (Interface + Impl)
                                                    ↓
                                           Repository Layer (JPA)
                                                    ↓
                                               MySQL 8
```

- **Estilo Arquitectónico:** Monolito Modular con patrón MVC.
- **Patrón Repository:** Cada entidad tiene su repositorio JPA que encapsula el acceso a datos. Los repositorios incluyen tanto métodos derivados de nombres como native queries para KPIs del dashboard.
- **Patrón Service Layer:** Separación entre interfaces e implementaciones. Los controladores dependen de interfaces, no de clases concretas, permitiendo desacoplamiento y testabilidad.
- **Patrón Dependency Injection:** Spring gestiona todas las dependencias vía @Autowired.
- **Seguridad:** Autenticación basada en HttpSession con verificación manual en cada controlador admin mediante atributo de sesión `perfil`. Passwords hasheadas con BCrypt y migración automática de credenciales legacy al arranque.
- **Transacciones:** Métodos críticos (creación de pedidos con detalles) anotados con `@Transactional` para garantizar atomicidad en operaciones multi-tabla.
- **PDF Generation:** BoletaService implementa generación programática de documentos PDF con OpenPDF, utilizando posicionamiento absoluto y tablas con estilos condicionales (zebra striping).

## 🚀 Requisitos Previos

- **JDK 17** o superior
- **Maven 3.8+**
- **MySQL 8**
- **IDE:** Eclipse o IntelliJ IDEA

## 🔧 Configuración y Ejecución

1. Clonar el repositorio:
```bash
git clone <url-del-repositorio>
cd PumaCafe
```

2. Configurar la base de datos:
```bash
mysql -u tu-usuario -p -e "CREATE DATABASE puma_cafe"
mysql -u tu-usuario -p puma_cafe < BD_PUMA_CAFE.txt
```

3. Configurar credenciales de base de datos:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Editar `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/puma_cafe
spring.datasource.username=tu-usuario
spring.datasource.password=tu-contraseña
```

4. Ejecutar la aplicación desde Eclipse:
```
Abrir PumaCafeJpaApplication.java → Run As → Java Application
```

5. Acceder en el navegador:
```
http://localhost:8080
```

