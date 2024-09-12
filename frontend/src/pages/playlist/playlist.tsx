import styles from './playlist.module.css';
import collage from '../../assets/collage.jpg';
import spotify from '../../assets/spotify.svg';
import seta from '../../assets/seta.svg';
import { ChevronLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import Footer from '../../components/footer/footer';

const PlaylistComponent = () => {
  const navigate = useNavigate();

  const handleBackClick = () => {
    navigate(-1);
  };

  const handleCreatePlaylist = async () => {
    try {
      const response = await fetch('http://localhost:8082/api/spotify/auth', {
        method: 'GET',
      });
      const data = await response.json();
      if (data && data.authUrl) {
        window.open(data.authUrl, '_blank');
      }
    } catch (error) {
      console.error('Erro ao criar playlist:', error);
    }
  };

  return <>
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
          <a href="#" className={styles.link}> clique aqui!</a>
        </p>
        <div className={styles.spotify}>
          <img
            src={collage}
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
  </>;
};

export default PlaylistComponent;