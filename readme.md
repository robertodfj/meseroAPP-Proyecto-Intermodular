# 🍽️ MeseroApp – Aplicación de Gestión de Bares y Comandas

**📧 Correo de pruebas:** [meseroapp1@gmail.com](mailto:meseroapp1@gmail.com)  
**🔑 Contraseña de prueba:** Mesero1234

---

## 📝 Descripción

**MeseroApp** es una aplicación Android desarrollada en **Java con Android Studio**, orientada a la **gestión completa de bares y sus comandas** en tiempo real. Permite a los usuarios:

- 🍺 Gestionar bares, mesas y productos.
- 📝 Crear y administrar pedidos/comandas por mesa.
- 📦 Controlar stock de productos.
- 📩 Enviar facturas por correo electrónico.
- 🔔 Recibir notificaciones de pedidos listos.
- 💾 Mantener persistencia de datos con Room y SharedPreferences.
- 🔄 Actualización automática de listas con LiveData.

La aplicación está pensada para ser **modular, escalable y de fácil uso**, combinando **Frontend y Backend** desde el diseño de UI hasta la lógica de negocio.

---

## ⚙️ Funcionalidades principales

- 🪑 **Gestión de mesas**: abrir/crear órdenes, cerrar órdenes, estados disponible/ocupada.
- 🛒 **Gestión de productos**: listar, crear, editar, control de stock.
- 🏷️ **Pedidos/comandas en tiempo real**: añadir líneas de pedido, calcular total automáticamente.
- 🔔 **Notificaciones**: avisos cuando los pedidos están listos.
- 📄 **Factura y email**: enviar facturas detalladas vía SMTP.
- 💾 **Persistencia**: Room + LiveData para datos en tiempo real, SharedPreferences para sesión y bar seleccionado.
- 🧩 **Modularidad**: fragmentos para login, registro, dashboard, mesas y productos.
- 🎨 **UI/UX**: diseño basado en prototipos Figma, planificación diagonal frontend/backend.

---

## 🛠️ Tecnologías usadas

- 💻 **Lenguaje**: Java
- 🛠️ **IDE**: Android Studio
- 🗄️ **Base de datos**: Room
- 📐 **Patrones**: MVVM, LiveData
- 📊 **UI**: RecyclerView, AlertDialog, Fragmentos
- 📧 **Correo**: SMTP con EmailSenderService
- 💾 **Persistencia**: SharedPreferences y Room
- 🎨 **Prototipo visual**: [Figma](https://www.figma.com/design/q4P4iXj0WNQ90FfZ5Qqfqb/Sin-t%C3%ADtulo?node-id=0-1&p=f&t=Ee0uwPHr2W9yOm0T-0)

---

## 🚀 Instalación y ejecución

- Clona el proyecto:

```bash
git clone <URL_DEL_REPOSITORIO>
```
- Abre el proyecto en Android Studio.
- Conecta un dispositivo Android o usa un emulador.
- Para pruebas rápidas, se puede usar la APK release generada:
```bash
app/build/outputs/apk/release/app-release.apk
```
Para instalar la APK:
- 🔓Habilita Instalar apps de orígenes desconocidos en el dispositivo.
- 📥Copia la APK y ábrela.

## Estructura del proyecto
	•	📌 Fragments: login, register, dashboard, mesas, productos.
	•	📋 RecyclerView: listado de mesas, productos y pedidos.
	•	🛎️ Services: EmailSenderService para envío de facturas.
	•	🗄️ Room: DAOs y Entities para persistencia.
	•	💾 SharedPreferences: guardar sesión y ID del bar.
	•	⚡ AlertDialog: interacciones rápidas con usuarios y pedidos.
## 📚 Documentación y prototipos
- 📝 Documentación técnica: Word con pruebas, flujo de la app y explicación de CRUD.
- 🎨 Prototipo UI: Figma￼
- 🗒️ Notas y recordatorios: funcionalidades implementadas, pendientes y mejoras futuras.


⸻
👤 Roberto de Frutos Jiménez  —  2026
📧 robertodfj93@gmail.com

