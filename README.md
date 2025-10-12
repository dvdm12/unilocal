<!-- ====================================================== -->
<!-- 🌎 README: UniLocal Mobile App -->
<!-- ====================================================== -->

<h1 align="center">
  <img src="./assets/unilocal.jpg" alt="UniLocal Logo" width="120"><br>
  UniLocal Mobile App
</h1>

<p align="center">
  <b>Descubre, comparte y apoya los negocios locales de tu ciudad</b><br>
  <sub>Aplicación móvil desarrollada con Kotlin y Jetpack Compose</sub>
</p>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original.svg" width="24" alt="Android Icon">
  Descripción general
</h2>

<p>
  <b>UniLocal</b> es una aplicación móvil que promueve el comercio local y la gastronomía regional, permitiendo a los usuarios
  descubrir lugares cercanos como restaurantes, cafeterías, sitios de comida rápida, museos y hoteles.  
  A través de una interfaz moderna e intuitiva, la aplicación ofrece un <b>mapa interactivo</b>, búsqueda y filtros por nombre,
  categoría o distancia, además de la posibilidad de dejar reseñas y calificaciones sobre los lugares visitados.
</p>

<p>
  Solo los usuarios registrados pueden crear y administrar nuevos lugares, mientras que un equipo de moderadores garantiza
  la autenticidad del contenido publicado.
</p>

<h3>Funciones destacadas:</h3>
<ul>
  <li>📍 Geolocalización en tiempo real</li>
  <li>🗺️ Mapa interactivo con marcadores dinámicos</li>
  <li>💬 Calificaciones y comentarios verificados</li>
  <li>🧭 Filtros avanzados de búsqueda</li>
  <li>📸 Carga de imágenes para cada lugar</li>
  <li>👥 Moderación de contenido generada por usuarios</li>
</ul>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="24" alt="Team Icon">
  Equipo del proyecto
</h2>

<table align="center">
  <tr>
    <th>Integrante</th>
    <th>Código</th>
  </tr>
  <tr>
    <td><b>David Mantilla Avilés</b></td>
    <td>240220212015</td>
  </tr>
  <tr>
    <td><b>Mariana Osorio Hernández</b></td>
    <td>24020211026</td>
  </tr>
  <tr>
    <td><b>Jhovanny Quiceno</b></td>
    <td>240220221047</td>
  </tr>
</table>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" width="24" alt="Tech Icon">
  Stack tecnológico
</h2>

<table align="center">
  <tr>
    <td align="center" width="200">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" width="60"><br>
      <b>Kotlin</b><br>
      <sub>v2.2.0</sub>
    </td>
    <td align="center" width="200">
      <img src="https://developer.android.com/static/images/logos/android.svg" width="55"><br>
      <b>Jetpack Compose</b><br>
      <sub>v1.11.0</sub>
    </td>
    <td align="center" width="200">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/androidstudio/androidstudio-original.svg" width="55"><br>
      <b>Android Studio</b><br>
      <sub>AGP 8.13.0</sub>
    </td>
  </tr>
</table>

<h3>Dependencias principales</h3>

<table>
  <tr><th>Dependencia</th><th>Versión</th></tr>
  <tr><td>Core KTX</td><td>1.17.0</td></tr>
  <tr><td>JUnit (unit tests)</td><td>4.13.2</td></tr>
  <tr><td>JUnit (AndroidX)</td><td>1.3.0</td></tr>
  <tr><td>Espresso Core (UI tests)</td><td>3.7.0</td></tr>
  <tr><td>Lifecycle Runtime KTX</td><td>2.9.3</td></tr>
  <tr><td>Compose BOM</td><td>2024.09.00</td></tr>
  <tr><td>Navigation Compose</td><td>2.9.1</td></tr>
  <tr><td>Kotlinx Serialization</td><td>1.9.0</td></tr>
</table>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" width="24" alt="Feature Icon">
  Características clave
</h2>

<table align="center">
  <tr>
    <td align="center" width="180">
      <img src="https://cdn-icons-png.flaticon.com/512/854/854878.png" width="40"><br>
      <b>Mapa interactivo</b>
    </td>
    <td align="center" width="180">
      <img src="https://cdn-icons-png.flaticon.com/512/622/622669.png" width="40"><br>
      <b>Búsqueda avanzada</b>
    </td>
    <td align="center" width="180">
      <img src="https://cdn-icons-png.flaticon.com/512/929/929564.png" width="40"><br>
      <b>Calificaciones</b>
    </td>
    <td align="center" width="180">
      <img src="https://cdn-icons-png.flaticon.com/512/2920/2920244.png" width="40"><br>
      <b>Subida de imágenes</b>
    </td>
  </tr>
</table>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-plain.svg" width="24" alt="Folder Icon">
  Estructura del proyecto
</h2>

<pre>
unilocal/
├── app/
│   ├── ui/
│   ├── data/
│   ├── model/
│   ├── viewmodel/
│   └── navigation/
├── assets/
│   └── unilocal.jpg
└── README.md
</pre>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="24" alt="Design Icon">
  Filosofía de diseño
</h2>

<p>
  UniLocal promueve una comunidad que <b>valora lo local</b>, integrando tecnología moderna con propósito social.  
  El desarrollo se guía por tres principios esenciales:
</p>

<ul>
  <li><b>Simplicidad:</b> interfaz clara, minimalista y directa.</li>
  <li><b>Escalabilidad:</b> arquitectura modular basada en ViewModel y Compose Navigation.</li>
  <li><b>Accesibilidad:</b> experiencia inclusiva, fluida y coherente.</li>
</ul>

<hr>

<h2>
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original-wordmark.svg" width="24" alt="License Icon">
  Licencia
</h2>

<p>
  Este proyecto fue desarrollado con fines académicos por estudiantes del programa de Ingeniería de Software.  
  Puedes usarlo, estudiarlo y adaptarlo libremente con fines educativos o demostrativos.
</p>

<hr>

<p align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg" width="20">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original.svg" width="20">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" width="20">
  <br>
  <b>Made with ❤️ using Kotlin + Jetpack Compose</b>
</p>

