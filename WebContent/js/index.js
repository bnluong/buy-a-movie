/**
 * Handles results from API call
 * @param resultData jsonObject
 */
function handleResult(resultData) {
	handleRandomMovies(resultData['randomMovies']);
	handleListOfGenres(resultData['listOfGenres']);
	handleBrowseByTitles();
}

/**
 * Handles random movies section
 * HTML Reference (#movie{ID}Img):
 * 		<img src="{URL}" alt="box-art-size-220x330" style="border-radius: 10px;" id="movie1Img" class="randomMoviesImg">
 * HTML Reference (#movie{ID}Title):
 * 		<a class="h6" href="movie-info.html?id={ID}"></a>
 * @param randomMovies jsonObject
 */
function handleRandomMovies(randomMovies) {
	for (let i = 0; i < randomMovies.length; i++) {
        let movieImgID = '#movie' + (i + 1) + 'Img';
		var imdbURL = 'https://www.imdb.com/title/' + randomMovies[i]['id'];
		$.ajax({
			url: 'http://www.whateverorigin.org/get?url=' + encodeURIComponent(imdbURL) + '&callback=?',
			dataType: 'json',
			async: false,
			success: function (data) {
				var tempDOM = document.createElement('div');
				tempDOM.innerHTML = data.contents;

				var poster = tempDOM.getElementsByClassName('poster');
				if (poster[0] != null) {
					let tempDOM = document.createElement('div');
					tempDOM.innerHTML = poster[0].innerHTML;
					let posterURL = tempDOM.querySelector('img').getAttribute('src');
					$(movieImgID).attr('src', posterURL);
				} else {
					$(movieImgID).attr('src', 'img/empty-box-art.png');
				}
			},
		});

		//  Handle random movie title
		let titleID = '#movie' + (i + 1) + 'Title';
		let titleURLID = '.title-' + i;
		let titleHTML = '<a class="h6 title-' + i + '" href=""></a>';
		let titleURLHTML = 'movie-info.html?id=' + randomMovies[i]['id'];
		$(titleID).append(titleHTML);
		$(titleURLID).attr('href', titleURLHTML);
		$(titleURLID).text(randomMovies[i]['title'] + ' (' + randomMovies[i]['year'] + ')');
	}
}

/**
 * Handles Browse by Genres section
 * HTML Reference (#browseByGenres):
 * 		<a class="btn btn-outline-dark btn-sm mx-1 my-1" href="login.html" role="button" style="border-radius:10px; font-size: 15px;"></a>
 * @param listOfGenres jsonArray
 */
function handleListOfGenres(listOfGenres) {
	for (let i = 0; i < listOfGenres.length; i++) {
		let btnHTML = '<a class="btn btn-outline-dark btn-sm mx-1 my-1 genreBtn-' + i + '" href="#" role="button" style="border-radius:15px; font-size: 18px;"></a>\n';
		let hrefHTML = 'browsing.html?' + 'title=none&genre=' + listOfGenres[i]['genre_name'] + '&sortBy=rating&order=desc&numResults=10&offset=0';
		let btnID = '.genreBtn-' + i;
		$('#browseByGenres').append(btnHTML);
		$(btnID).attr('href', hrefHTML);
		$(btnID).text(listOfGenres[i]['genre_name']);
	}
}

/**
 * Handles Browse by Titles section
 * HTML Reference (#browseByTitleNumeric):
 * 		<a href="#" class="badge badge-dark badge-pill" style="font-size: 15px;"></a>
 * HTML Reference (#browseByTitleAlpha):
 * 		<a href="#" class="badge badge-dark badge-pill" style="font-size: 15px;"></a>
 * @param None
 */
function handleBrowseByTitles() {
	const numArray = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
	for (let i = 0; i < numArray.length; i++) {
		let bdgHTML = '<a href="#" class="badge badge-dark badge-pill mx-1 my-1 numBdg-' + i + '" style="font-size: 18px;"></a>\n';
		let hrefHTML = 'browsing.html?' + 'title='+ numArray[i] + '&genre=none' + '&sortBy=rating&order=desc&numResults=10&offset=0';
		let bdgID = '.numBdg-' + i;
		$('#browseByTitleNumeric').append(bdgHTML);
		$(bdgID).attr('href', hrefHTML);
		$(bdgID).text(numArray[i]);
	}

	const alphaArray = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
	for (let i = 0; i < alphaArray.length; i++) {
		let bdgHTML = '<a href="#" class="badge badge-dark badge-pill mx-1 my-1 alpBdg-' + i + '" style="font-size: 18px;"></a>\n';
		let hrefHTML = 'browsing.html?' + 'title='+ alphaArray[i] + '&genre=none' + '&sortBy=rating&order=desc&numResults=10&offset=0';
		let bdgID = '.alpBdg-' + i;
		$('#browseByTitleAlpha').append(bdgHTML);
		$(bdgID).attr('href', hrefHTML);
		$(bdgID).text(alphaArray[i]);
	}
}

$.ajax('api/index', {
	method: 'GET',
	success: handleResult,
});
