document.addEventListener("DOMContentLoaded", function() {
    const tabsBox = document.querySelector(".tabs-box"),
    allTabs = tabsBox.querySelectorAll(".tab"),
    arrowIcons = document.querySelectorAll(".icon i");
    let isDragging = false;

    const handleIcons = (scrollVal) => {
        let maxScrollableWidth = tabsBox.scrollWidth - tabsBox.clientWidth;
        arrowIcons[0].parentElement.style.display = scrollVal <= 0 ? "none" : "flex";
        arrowIcons[1].parentElement.style.display = maxScrollableWidth - scrollVal <= 1 ? "none" : "flex";
    }

    arrowIcons.forEach(icon => {
        icon.addEventListener("click", () => {
            // if clicked icon is left, reduce 350 from tabsBox scrollLeft else add
            let scrollWidth = tabsBox.scrollLeft += icon.id === "left" ? -340 : 340;
            handleIcons(scrollWidth);
        });
    });

    allTabs.forEach(tab => {
        tab.addEventListener("click", () => {
            tabsBox.querySelector(".active").classList.remove("active");
            tab.classList.add("active");
        });
    });

    const dragging = (e) => {
        if(!isDragging) return;
        tabsBox.classList.add("dragging");
        tabsBox.scrollLeft -= e.movementX;
        handleIcons(tabsBox.scrollLeft)
    }

    const dragStop = () => {
        isDragging = false;
        tabsBox.classList.remove("dragging");
    }

    tabsBox.addEventListener("mousedown", () => isDragging = true);
    tabsBox.addEventListener("mousemove", dragging);
    document.addEventListener("mouseup", dragStop);
});

// Rest of your JavaScript code...

// Function to search for videos by keyword
function searchByKeyword() {
    const searchInput = document.getElementById("search-input");
    const searchQuery = searchInput.value.trim();

    if (searchQuery === "") {
        alert("Please enter a search keyword.");
        return;
    }

    const apiKey = "AIzaSyBxcRR50UNLjvb2vV0pRiw8kR6PoaYYWQw"; 
    const maxResults = 8; 
    const apiUrl = `https://www.googleapis.com/youtube/v3/search?key=${apiKey}&part=snippet&type=video&videoDuration=short&maxResults=${maxResults}&q=${searchQuery}`;

    // Make a GET request to the YouTube Data API
    fetch(apiUrl)
        .then((response) => response.json())
        .then((data) => {
            const videos = data.items;
            const videoContainer = document.querySelector(".youtube-container");
            videoContainer.innerHTML = ""; // Clear previous search results

            for (const video of videos) {
                const videoThumbnail = document.createElement("div");
                videoThumbnail.classList.add("col-lg-4", "col-md-6", "mb-4");

                const videoUrl = `https://www.youtube.com/embed/${video.id.videoId}`;

                videoThumbnail.innerHTML = `
                    <div class="video-info">
                        <div class="embed-responsive embed-responsive-16by9">
                            <img src="${video.snippet.thumbnails.high.url}" alt="${video.snippet.title}" class="img-thumbnail embed-responsive-item">
                            <span class="play-icon" onclick="playVideo('${videoUrl}')"><i class="fa fa-youtube-play" style="font-size:48px;color:red"></i></span>
                        </div>
                        <h4 class="video-title">${video.snippet.title}</h4>
                    </div>
                `;

                videoContainer.appendChild(videoThumbnail);
            }
        })
        .catch((error) => {
            console.error("Error fetching YouTube data:", error);
        });
}

// Function to display the video pop-up with the specified video URL
function showVideoPopup(videoUrl) {
    const videoPopup = document.getElementById("video-popup");
    const videoIframe = document.getElementById("video-iframe");
    
    videoIframe.src = videoUrl + "?autoplay=1";
    videoIframe.style.width = "800px"; 
    videoIframe.style.height = "450px"; 

    
    videoPopup.style.display = "flex"; // Use flex display for vertical and horizontal centering
    videoPopup.style.alignItems = "center"; // Center vertically
    videoPopup.style.justifyContent = "center"; // Center horizontally
}

// Function to hide the video pop-up
function hideVideoPopup() {
    const videoPopup = document.getElementById("video-popup");
    const videoIframe = document.getElementById("video-iframe");

    videoIframe.src = "";
    videoPopup.style.display = "none"; // Hide the popup
}

// Function to play the video
function playVideo(videoUrl) {
    const videoIframe = document.getElementById("video-iframe");
    videoIframe.src = videoUrl + "?autoplay=1";
    showVideoPopup(videoUrl);
}

