import styles from './footer.module.css';
import { Twitter, Github } from 'lucide-react';

const Footer = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.footerContainer}>
        <div className={styles.footerLinks}>
          <a href="#about" className={styles.footerLink}>About Us</a>
          <a href="#contact" className={styles.footerLink}>Contact</a>
        </div>
        <div className={styles.footerSocials}>
          <a href="https://github.com/kropsz" target="_blank" rel="noopener noreferrer" className={styles.footerSocialLink}>
            <Github />
            GitHub

          </a>
          <a href="https://x.com/kropsz" target="_blank" rel="noopener noreferrer" className={styles.footerSocialLink}>
            <Twitter />
            Twitter
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;