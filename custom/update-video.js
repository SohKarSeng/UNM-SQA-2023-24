
// Keep track of whether notes have been displayed for each video
const notesDisplayed = new Array(12).fill(false);


for (let i = 0; i < 12; i++) {
    //save note
    $('#saveNoteButton' + i).on('click', function () {
        let videoID = videoIDs[i];
        let time = convertTimeStampToSeconds(getVideoTimestamp(i));
        let note_key = generateNoteKey(videoID, time);
        saveNote(i, note_key);

        // Display notes only if not displayed before
        if (!notesDisplayed[i]) {
            showAllNoteContent(videoIDs[i], i);
            notesDisplayed[i] = true;
        }

        refreshDropdown(i);
    });

    $('#note-dropdown-' + i).on('change', function () {
        players[i].pause();
        $('#video-timestamp-' + i).html(convertSecondsToTimestamp($('#note-dropdown-' + i).val()));
        $('#video-timestamp-' + i).trigger('change');
    });

    $('#video-timestamp-' + i).on('change', function () {
        let timestamp = $('#video-timestamp-' + i).html();

        // look for the option where html is timestamp
        $('#note-dropdown-' + i + ' option').filter(function () {
            return $(this).html() == timestamp;
        }).prop('selected', true);

        let note_key = generateNoteKey(videoIDs[i], convertTimeStampToSeconds(timestamp));
        let note_content = localStorage.getItem(note_key);
        $('#note-content-' + i).val(note_content);
    });

    $('#note-content-' + i).on('focus', function () {
        players[i].pause();
    });

    $('#shareNoteButton-' + i).on('click', function () {
        let videoID = videoIDs[i];
        let time = convertTimeStampToSeconds(getVideoTimestamp(i));
        let note_key = generateNoteKey(videoID, time);
        shareNote(i, note_key);
    });

    // ... (other event handlers)
}

function defaultVideo() {
    let url = "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&type=video&maxResults=12&videoDuration=short&part=snippet&q=Software%20Quality%20Assurance";

    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            for (let i = 0; i < 12; i++) {
                videoIDs[i] = data.items[i].id.videoId;
                $('#videoPlayer' + i).attr('data-plyr-embed-id', videoIDs[i]);
                initializePlyr(i);

                $('#videoTitle' + i).html(data.items[i].snippet.title);
            }
        }
    });

}

function initializePlyr(playerID) {
    let intervals = [];

    players[playerID] = new Plyr('#videoPlayer' + playerID, {
        type: 'youtube'
    });

    players[playerID].on('playing', event => {
        // log the time every second
        intervals[playerID] = setInterval(() => {
            let time = players[playerID].currentTime.toFixed(0);
            $('#video-timestamp-' + playerID).html(convertSecondsToTimestamp(time));
            $('#video-timestamp-' + playerID).trigger('change');
        }, 500);
    });

    players[playerID].on('pause', event => {
        // stop the interval
        console.log("pause")
        clearInterval(intervals[playerID]);
    });

    // Add event listener for timeupdate event
    players[playerID].on('timeupdate', event => {
        let currentTime = players[playerID].currentTime.toFixed(0);
        let timestamp = convertSecondsToTimestamp(currentTime);
        let descriptionList = $('#description-list-' + playerID);
        let notes = descriptionList.find('dt');
        let noteDropdown = $('#note-dropdown-' + playerID);
        let noteContent = $('#note-content-' + playerID);

        for (let i = 0; i < notes.length; i++) {
            let noteTimestamp = $(notes[i]).html();
            let noteContent = $(notes[i]).next();
            $(notes[i]).css('height', '90%'); // Add height style rule
            if (noteTimestamp === timestamp) {
                $(notes[i]).css('background-color', '#ffffcc');
                noteDropdown.find('option[value="' + currentTime + '"]').prop('selected', true);
                noteContent.css('background-color', '#ffffcc');
            } else {
                $(notes[i]).css('background-color', 'transparent');
                noteDropdown.find('option[value="' + currentTime + '"]').prop('selected', false);
                noteContent.css('background-color', 'transparent');
            }
        }
    });

    refreshDropdown(playerID);
    showAllNoteContent(videoIDs[playerID], playerID);
}

function convertSecondsToTimestamp(seconds) {
    let minutes = Math.floor(seconds / 60);
    let secondsLeft = seconds % 60;
    if (secondsLeft < 10) {
        secondsLeft = "0" + secondsLeft;
    }
    return minutes + ":" + secondsLeft;
}

// Function to change YouTube video source
function changeVideo(playerID, videoID, videoTitle) {
    // Destroy the existing player
    players[playerID].destroy();

    $('#videoPlayer' + playerID).attr('data-plyr-embed-id', videoID);
    initializePlyr(playerID);

    // Change the title
    $('#videoTitle' + playerID).html(videoTitle);
}

function pauseUserInput(time) {
    $('#section-block').block({
        message: '<div class="spinner-border text-primary" role="status"></div>',
        timeout: time,
        css: {
            backgroundColor: 'transparent',
            border: '0'
        },
        overlayCSS: {
            backgroundColor: '#fff',
            opacity: 0.8
        }
    });
}

function getFinalQuery() {
    let query = "";

    let keyword = $('#keyword-search').val();
    let keywordsCategory = $('#category-select').val();

    for (const keyword in keywordsCategory) {
        keywordsCategory[keyword] = keywordsCategory[keyword].replace(/ /g, "%20")
        query += keywordsCategory[keyword] + "%2C";
    }
    return (query += keyword);
}

function getVideoTimestamp(key) {
    return $('#video-timestamp-' + key).html();
}

function generateNoteKey(videoID, time) {
    return videoID + "___" + time;
}

function convertTimeStampToSeconds(timestamp) {
    let minutes = timestamp.split(":")[0];
    let seconds = timestamp.split(":")[1];
    return parseInt(minutes) * 60 + parseInt(seconds);
}

function saveNote(key, note_key) {
    let note_content = $('#note-content-' + key).val();
    console.log("Note content: " + note_content);

    // Check if the new note content is empty
    if (note_content.trim() === "") {
        // If it's empty, remove the existing note
        localStorage.removeItem(note_key);
    } else {
        // If it's not empty, save the new note
        localStorage.setItem(note_key, note_content);
    }

    // Clear existing notes before displaying new ones
    $('#description-list-' + key).empty();

    // Display all notes
    showAllNoteContent(videoIDs[key], key);

    refreshDropdown(key);
}

function refreshDropdown(key) {
    let note_dropdown = $('#note-dropdown-' + key);
    note_dropdown.empty();
    note_dropdown.append('<option value="">Select a note</option>');

    let note_keys = [];
    for (let i = 0; i < localStorage.length; i++) {
        let note_key = localStorage.key(i);
        if (note_key.includes(videoIDs[key])) {
            note_keys.push(note_key);
        }
    }

    note_keys.sort();

    for (let i = 0; i < note_keys.length; i++) {
        let note_key = note_keys[i];
        let note_timestamp = note_key.split("___")[1];
        note_dropdown.append('<option value="' + note_timestamp + '">' + convertSecondsToTimestamp(note_timestamp) + '</option>');
    }
}

function searchVideo() {
    pauseUserInput(5000);

    let url = "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY + "&type=video&maxResults=12&videoDuration=short&part=snippet&q=";

    let final_query = getFinalQuery();

    if (final_query != "") {
        url += final_query;
    } else {
        url += "Software%20Quality%20Assurance";
    }

    // get the videos
    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            for (let i = 0; i < 12; i++) {
                videoIDs[i] = data.items[i].id.videoId;
                changeVideo(i, data.items[i].id.videoId, data.items[i].snippet.title);
                $('#video-timestamp-' + i).html("0:00");
                $('#note-content-' + i).val("");
            }
        }
    });
}

// share all notes of that video in clipboard with video url
function shareNote(key, note_key) {
    let note_content = $('#note-content-' + key).val();
    let video_url = "https://www.youtube.com/watch?v=" + videoIDs[key];
    let note_url = video_url + "&note=" + note_key;
    let note_timestamp = convertSecondsToTimestamp(note_key.split("___")[1]);

    let notes = "";
    for (let i = localStorage.length - 1; i >= 0; i--) {
        let note_key = localStorage.key(i);
        if (note_key.includes(videoIDs[key])) {
            let note_content = localStorage.getItem(note_key);
            let note_timestamp = convertSecondsToTimestamp(note_key.split("___")[1]);
            notes += note_timestamp + " " + note_content + "\n";
        }
    }

    navigator.clipboard.writeText("Video URL : " + note_url + "\n\n" + "Notes : " + "\n" + notes).then(function () {
        console.log('Async: Copying to clipboard was successful!');
    }, function (err) {
        console.error('Async: Could not copy text: ', err);
    });
}
function showAllNoteContent(video_id, key) {
    // Clear existing notes before displaying new ones
    $('#description-list-' + key).empty();

    for (let i = localStorage.length - 1; i >= 0; i--) {
        let note_key = localStorage.key(i);
        if (note_key.includes(video_id)) {
            let note_content = localStorage.getItem(note_key);
            let note_timestamp = convertSecondsToTimestamp(note_key.split("___")[1]);

            let dt = '<dt class="col-3">' + note_timestamp + '</dt>';
            let dd = '<dd class="col-9">' + note_content + '</dd>';
            $('#description-list-' + key).append(dt + dd);
        }
    }
}


for (let i = 0; i < 12; i++) {
    $('#clearallNoteButton-' + i).on('click', function () {
        // Prompt the user for confirmation
        const userConfirmed = window.confirm("Are you sure you want to clear all notes for this video?");

        if (userConfirmed) {
            clearAllNotes(i);
        }
    });
}


function clearAllNotes(index) {
    for (let i = localStorage.length - 1; i >= 0; i--) {
        let key = localStorage.key(i);
        if (key.includes(`___${index}`)) {
            localStorage.removeItem(key);
        }
    }

    // Clear the notes displayed on the page
    $(`#description-list-${index}`).empty();

    // Clear the entire local storage
    localStorage.clear();

    refreshDropdown(index);
}