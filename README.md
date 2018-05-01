# popular-movies

<p>This app allows users to discover the most popular movies playing and/or the top rated ones.</p>

<h2>Project Overview</h2>
<ul>
  <li>The app displays a grid arrangement of movie posters + movie titles upon launch</li>
  <li>It allows users to change sort order (top rated vs most popular) via a setting menu-entry (filter setting icon on top right corner)</li>
  <li>It allows users to tap on a movie poster and transtitions to a details screen with additional information such as:</li>
  <ul>
    <li>Movie backdrop image thumbnail</li>
    <li>Original title</li>
    <li>Movie poster image thumbnail</li>
    <li>Movie release date</li>
    <li>Movie genres</li>
    <li>A plot synopsis (called overview in the api)</li>
    <li>user rating (called vote_average in the api)</li>
  </ul>
</ul>

<h2>Remarks</h2>
<p>The user shall set his own TMDB API key on passwords.xml file before building and running project.</p>

<h2>Tech. Caracteristics</h2>
<ul>
  <li>The app fetchs data from the Internet with theMovieDB API</li>
  <li>It incorporates retrofit + gson libraries to simplify internet taks</li>
  <li>It displays AlertDialogs to warn users about internet issues (no internet connectivity) and/or TMDB API key issues (no API key)
  <li>It incorporates butterknife library to bind views</li>
  <li>It loads movie poster and backdrop images using Picasso library</li>
  <li>Item position on GridView (MainActivity) and detailed movie info in detail activity are stored during screen rotation</li>
</ul>

<h2>App Mockup (WIP)</h2>
<a href="https://invis.io/F3HR4FZJQWA">App Mockup</a>

