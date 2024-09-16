import React from 'react';

interface SpotifyEmbedProps {
  playlistLink: string;
}

const SpotifyEmbed: React.FC<SpotifyEmbedProps> = ({ playlistLink }) => {
  const extractPlaylistId = (link: string) => {
    const regex = /playlist\/([a-zA-Z0-9]+)(\?.*)?$/;
    const match = link.match(regex);
    return match ? match[1] : '';
  };

  const playlistId = extractPlaylistId(playlistLink);
  const embedUrl = `https://open.spotify.com/embed/playlist/${playlistId}`;

  return (
    <iframe
      src={embedUrl}
      width="300"
      height="380"
      allow="encrypted-media"
      title="Spotify Playlist"
    ></iframe>
  );
};

export default SpotifyEmbed;