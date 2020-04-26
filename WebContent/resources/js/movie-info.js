/**
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */

/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
	// Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Use regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"), results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleResult(resultData) {
    console.log("handleResult: populating movie info from resultData");

    // Populate the page title <title id="title"> with the movie's title
    let titleHTML = jQuery("#title");
    titleHTML.append(resultData[0]["movie_title"]);
    
    // Populate the <h1 id="movie_title"> with the movie's title
    let movieTitleHTML = jQuery("#movie_title");
    movieTitleHTML.append(resultData[0]["movie_title"]);

    // Populate the <tbody id=movie_info_table_body> with the movie's infomation
    let movieInfoTableBodyHTML = jQuery("#movie_info_table_body");
    let infoHTML = "";
    infoHTML += "<tr>";
    infoHTML += "<td>ID #</td>";
    infoHTML += "<td>"+ resultData[0]["movie_id"] + "</td>";
    infoHTML += "</tr>";
    
    infoHTML += "<tr>";
    infoHTML += "<td>Year</td>";
    infoHTML += "<td>"+ resultData[0]["movie_year"] + "</td>";
    infoHTML += "</tr>";
    
    infoHTML += "<tr>";
    infoHTML += "<td>Director</td>";
    infoHTML += "<td>"+ resultData[0]["movie_director"] + "</td>";
    infoHTML += "</tr>";
    
    infoHTML += "<tr>";
    infoHTML += "<td>Genres</td>";
    infoHTML += "<td>"+ resultData[0]["movie_genres"] + "</td>";
    infoHTML += "</tr>";
    
    infoHTML += "<tr>";
    infoHTML += "<td>Stars</td>";
    infoHTML += "<td>"+ resultData[0]["movie_stars"] + "</td>";
    infoHTML += "</tr>";
    
    infoHTML += "<tr>";
    infoHTML += "<td>Rating</td>";
    infoHTML += "<td>"+ resultData[0]["movie_rating"] + "</td>";
    infoHTML += "</tr>";
    
    movieInfoTableBodyHTML.append(infoHTML);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */
// Get id from URL
let movieId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  						// Setting return data type
    method: "GET",							// Setting request method
    url: "api/movie-info?id=" + movieId, 	// Setting request url, which is mapped by the servlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the the servlet
});