/**
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data
 * into html elements
 * 
 * @param resultData
 *            jsonObject
 */
function handleMovieListResult(resultData) {
    console.log("handleMovieListResult: populating movie list table from resultData");

    // Populate the movie list table
    // Find the empty table body by id "movie_list_table_body"
    let movieListTableBodyElement = jQuery("#movie_list_table_body");

    // Iterate through resultData, no more than 10 entries
    for(let i = 0; i < resultData.length; i++) {
        // Concatenate the html tags with resultData JsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
                
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "<th>";
        rowHTML += '<a href="movie-info.html?id=' + resultData[i]["movie_id"] + '">' + resultData[i]["movie_title"] + "</a>";
        rowHTML += "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        
        rowHTML += "<th>";
        // Add hyperlink to a star's name by the star's id)
        var stars = resultData[i]["movie_stars"];				// movie_stars entry is a JSON array
        for(let j = 0; j < stars.length; j++)
        	rowHTML += '<a href="star-info.html?id=' + stars[j]["star_id"] + '">' + stars[j]["star_name"] + "</a>" + ", ";
        // Regular expression to remove trailing commas and whitespaces
        rowHTML = rowHTML.replace(/,\s*$/, "");
        rowHTML += "</th>";
        
        rowHTML += "<th>" + resultData[i]["movie_genres"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieListTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
// Makes the HTTP GET request and registers on success callback function
// handleMovieListResult
jQuery.ajax({
    dataType: "json", 		// Setting return data type
    method: "GET", 			// Setting request method
    url: "api/movie-list", 	// Setting request url, which is mapped by StarsServlet in Stars.java
    // Setting callback function to handle data returned successfully by the MovieListServlet
    success: (resultData) => handleMovieListResult(resultData)
});