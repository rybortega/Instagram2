# Project 4 - Instagram2

**Parstagram** is a photo sharing app using Parse as its backend.

Time spent: **20** hours spent in total

## User Stories

The following **required** functionality is completed:

- [ ] User sees app icon in home screen.
- [ ] User can sign up to create a new account using Parse authentication
- [ ] User can log in and log out of his or her account
- [ ] The current signed in user is persisted across app restarts
- [ ] User can take a photo, add a caption, and post it to "Instagram"
- [ ] User can view the last 20 posts submitted to "Instagram"
- [ ] User can pull to refresh the last 20 posts submitted to "Instagram"
- [ ] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [ ] Style the login page to look like the real Instagram login page.
- [ ] Style the feed to look like the real Instagram feed.
- [ ] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [ ] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [ ] Show the username and creation time for each post
- [ ] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [ ] Allow the logged in user to add a profile photo
  - [ ] Display the profile photo with each post
  - [ ] Tapping on a post's username or profile photo goes to that user's profile page
  - [ ] User Profile shows posts in a grid view
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [ ] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [x] Home Feed: User can Like and clicking Comment icon opens post detail activity and ***focuses*** on typing a new comment.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Enable push notifications of comments/likes on your post
2. Be able to follow people and see only their posts in the Home Feed, as opposed to everybody's

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='parstagram.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

As always, it took a minute to get rolling, but once I really got interested it went well.

## License

    Copyright 2020 Wilmer Alexander

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
