import styles from './playlist.module.css';
import spotify from '../../assets/spotify.svg';
import seta from '../../assets/seta.svg';
import { ChevronLeft } from 'lucide-react';
import { useNavigate, useLocation } from 'react-router-dom';
import Footer from '../../components/footer/footer';
import axios from 'axios';
import { useEffect, useState } from 'react';

const PlaylistComponent = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [link, setLink] = useState(location.state?.link || localStorage.getItem('collageLink') || '');

  const handleBackClick = () => {
    navigate(-1);
  };

  const handleCreatePlaylist = async () => {
    try {
      localStorage.setItem('collageLink', link);

      const response = await axios.get('http://localhost:8082/api/login');
      const authUrl = `${response.data}?redirect_uri=http://localhost:8082/api/get-user-code`;
      if (authUrl) {
        window.location.href = authUrl;
      }
    } catch (error) {
      console.error('Erro ao criar playlist:', error);
    }
  };

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const isAuthenticated = params.get('authenticated');

    if (isAuthenticated) {
      const createPlaylist = async () => {
        try {
          await axios.post('http://localhost:8082/api/playlists/create/kropsky212');
          console.log('Playlist criada com sucesso');
        } catch (error) {
          console.error('Erro ao criar playlist:', error);
        }
      };

      createPlaylist();
    }
  }, [location.search]);

  useEffect(() => {
    const storedLink = localStorage.getItem('collageLink');
    if (storedLink) {
      setLink(storedLink);
    }
  }, []);

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
        </div>
      </div>
      <Footer />
    </>
  );
};

export default PlaylistComponent;