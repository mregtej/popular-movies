# popular-movies

<p>This app allows users to discover the most popular movies playing and/or the top rated ones. It also allows users to mark films as favorites and to display the favorite films collection.</p>

<h2>Project Overview</h2>
<ul>
  <li>The app displays a grid arrangement of movie posters + movie titles upon launch</li>
  <li>It allows users to change sort order (top rated, most popular or favorites) via a setting menu-entry (filter setting icon on top right corner)</li>
  <li>The current film sorting choice is stored in SharedPreferences for recovering last user choice in consecutive app runs.</li>
  <li>It allows users to tap on a movie poster and transtitions to a details screen with additional information such as:</li>
  <ul>
    <li>Movie backdrop image thumbnail</li>
    <li>Original title</li>
    <li>Movie poster image thumbnail</li>
    <li>Movie release date</li>
    <li>Movie genres</li>
    <li>A plot synopsis (called overview in the api)</li>
    <li>user rating (called vote_average in the api)</li>
    <li>Add/remove to/from favorites star button</li>
      <ul>
        <li> The user can tap on button for adding/removing film to/from favorites depending on the film is already (or not) included in favorite films collection</li>
      </ul>
    <li> Movie trailers (displayed in an horizontal scrollable gridview)</li>
      <ul>
        <li> The user can tap on film play image to display the video trailer either in the Youtube app or a web browser</li>
      </ul>
    <li> Movie reviews (displayed in an vertical scrollable gridview)</li>
      <ul>
        <li> The user can expand/colapse review comments which show "..." at the end of the comment-text by just tapping on the textview</li>
      </ul>
  </ul>
</ul>

<h2>Remarks</h2>
<p>The user shall set his own TMDB API key on passwords.xml file before building and running project.</p>

<h2>Tech. Caracteristics</h2>
<ul>
  <li>The app fetchs data from the Internet with theMovieDB API</li>
  <li>It incorporates retrofit + gson libraries to simplify internet taks</li>
  <li>Architectural elements such as Repository, ViewModels and LiveData classes have been implemented</li>
  <li>The app runs the online (theMovieDB API) and offine (favorite movies DB) requests in separated threads.</li> 
  <li>It displays AlertDialogs to warn users about internet issues (no internet connectivity) and/or TMDB API key issues (no API key)
  <li>It incorporates butterknife library to bind views</li>
  <li>It loads movie poster, backdrop images and movie trailer play icons using Picasso library</li>
  <li>Grid data and item position on MainActivity's GridView are stored during screen rotation (savedInstance) for avoiding unnecesary API calls to theMovieDB</li>
  <li>Detailed movie info and film trailer position in detail activity are stored during screen rotation (savedInstance) for avoiding unnecesary API calls to theMovieDB</li>
  <li>A favorite movies DB and content provider (data in app is not retrieved via content provider due to the implementation of ViewModel + LiveData pattern) have been created to store the favorite movies (TMDBFilm parcelable)</li>
</ul>

<h2>Architecture Description</h2>
https://user-images.githubusercontent.com/10979956/40702141-ef24dc5e-63e0-11e8-8f6b-bf0766a0ac1f.png

<h2>App Mockup (WIP)</h2>
<a href="https://invis.io/F3HR4FZJQWA">App Mockup</a>
