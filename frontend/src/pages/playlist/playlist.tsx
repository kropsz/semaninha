import styles from './playlist.module.css';
import spotify from '../../assets/spotify.svg';
import seta from '../../assets/seta.svg';
import { ChevronLeft } from 'lucide-react';
import { useNavigate, useLocation } from 'react-router-dom';
import Footer from '../../components/footer/footer';
import axios from 'axios';
import { useState } from 'react';
import { CircularProgress } from '@mui/material';
import SpotifyEmbed from '../../components/playlist-embed/embed';

const PlaylistComponent = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [link] = useState(location.state?.link || localStorage.getItem('collageLink') || '');
  const [user] = useState(location.state?.user || localStorage.getItem('user') || '');
  const [isLoading, setIsLoading] = useState(false);
  const [playlistLink, setPlaylistLink] = useState('');
  const [suggestion, setSuggestion] = useState('');
  const [hasCreatedPlaylist, setHasCreatedPlaylist] = useState(false);

  localStorage.setItem('collageLink', link);
  localStorage.setItem('user', user);

  const handleBackClick = () => {
    navigate('/home');
  };

  const handleCreatePlaylist = async () => {
    try {
      const response = await axios.get('http://localhost:8082/api/login');
      const authUrl = `${response.data}?redirect_uri=http://localhost:8082/api/get-user-code`;

      if (authUrl) {
        const authWindow = window.open(authUrl, 'SpotifyLogin', 'width=500,height=600');

        window.addEventListener('message', (event) => {
          if (event.data === 'authenticated') {
            console.log('Autenticação concluída.');
            createPlaylistIfAuthenticated();
          }
        });

        const authCheck = setInterval(() => {
          if (authWindow && authWindow.closed) {
            clearInterval(authCheck);
            console.log('Janela de autenticação fechada.');
          }
        }, 500);
      }
    } catch (error) {
      console.error('Erro ao autenticar com o Spotify:', error);
    }
  };


  const createPlaylistIfAuthenticated = async () => {
    if (!hasCreatedPlaylist) {
      console.log('Creating playlist...');
      setIsLoading(true);
      try {
        const response = await axios.post(`http://localhost:8082/api/playlists/create/${user}`);
        console.log('Playlist criada com sucesso');
        setPlaylistLink(response.data);
        setHasCreatedPlaylist(true);
      } catch (error) {
        console.error('Erro ao criar playlist:', error);
      } finally {
        setIsLoading(false);
      }
    }
  };

  const handleSuggestionPlaylist = async () => {
    try {
      const response = await axios.post(`http://localhost:8082/api/playlists/recommend/${user}`);
      console.log('Playlist de Sugestão criada com sucesso');
      setSuggestion(response.data);
    } catch (error) {
      console.error('Erro ao criar playlist de sugestão:', error);
    }
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.content}>
          <div className={styles.headers}>
            <button onClick={handleBackClick} className={styles.backButton}>
              <ChevronLeft className={styles.back} />
            </button>
            <h1 className={styles.title}>Sua Semaninha</h1>
          </div>
          <p className={styles.description}>
            Faça agora mesmo o Download da sua colagem e<br /> compartilhe com seus amigos!
            <a href={link} className={styles.link} download> clique aqui!</a>
          </p>
          <div className={styles.spotify}>
            <img
              src={link}
              alt="Album cover"
              className={styles.image}
            />
            <img className={styles.seta} src={seta} alt="" />
          </div>
        </div>
        <div className={styles.playlist}>
          {isLoading ? (
            <CircularProgress className={styles.loading} />
          ) : (
            <>
              {suggestion ? (
                <div className={styles.playlistComponent}>
                  <SpotifyEmbed playlistLink={suggestion} />
                  <p>Espero que goste das nossas sugestões e aproveite a playlist !!! 🤗🤗</p>
                </div>
              ) : (
                <>
                  {playlistLink ? (
                    <div className={styles.playlistComponent}>
                      <SpotifyEmbed playlistLink={playlistLink} />
                      <div className='suggestion'>
                        <p className='description'>Deseja criar uma playlist apenas com sugestões do semaninha baseado no que você anda curtindo 👀?</p>
                        <button className='btn' onClick={handleSuggestionPlaylist}>ACEITAR SUGESTÃO</button>
                      </div>
                    </div>
                  ) : (
                    <>
                      <img
                        src={spotify}
                        alt="Spotify Icon"
                        className={styles.icon}
                      />
                      <p className={styles.playlistDescription}>
                        Se você gostou da colagem, crie agora mesmo a playlist no Spotify e aproveite ainda mais essa experiência
                      </p>
                      <button className={styles.button} onClick={handleCreatePlaylist}>
                        CRIAR PLAYLIST 🎶
                      </button>
                    </>
                  )}
                </>
              )}
            </>
          )}
        </div>
      </div>
      <Footer />
    </>
  );
};

export default PlaylistComponent;