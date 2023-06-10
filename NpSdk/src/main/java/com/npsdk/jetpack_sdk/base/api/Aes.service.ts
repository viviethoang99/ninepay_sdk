import CryptoJS from 'crypto-js';
import { publicEncrypt, constants } from 'crypto-browserify';

class AesService {
  private _rk: string;
  private _rke: string;
  private readonly pk: string;

  constructor(pk: string) {
    this._rk = '';
    this._rke = '';
    this.pk = pk;

    this.randomKey = this.randomKey.bind(this);
    this.encryptRandomKey = this.encryptRandomKey.bind(this);
    this.aesEncrypt = this.aesEncrypt.bind(this);
    this.aesDecrypt = this.aesDecrypt.bind(this);

    this.randomKey();
  }

  get rke() {
    return this._rke;
  }

  private randomKey() {
    const stringRamdom = new Date().valueOf();
    this._rk = CryptoJS.MD5(stringRamdom.toString()).toString();
    this.encryptRandomKey();
  }

  private encryptRandomKey() {
    if (!this.pk) {
      throw new Error('No public key available');
    }
    const fullPublicKey = `-----BEGIN PUBLIC KEY-----${this.pk}-----END PUBLIC KEY-----`;
    const encrypted = publicEncrypt(
      { key: fullPublicKey, padding: constants.RSA_PKCS1_PADDING },
      Buffer.from(this._rk)
    );
    this._rke = encrypted.toString('base64');
  }

  aesEncrypt(data: any) {
    const key = CryptoJS.enc.Utf8.parse(this._rk); // Use Utf8-Encoder.
    const iv = CryptoJS.lib.WordArray.create();

    const encrypted = CryptoJS.AES.encrypt(JSON.stringify(data), key, {
      iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });

    return encrypted.toString();
  }

  aesDecrypt(data: string) {
    const key = CryptoJS.enc.Utf8.parse(this._rk); // Use Utf8-Encoder.
    const iv = CryptoJS.lib.WordArray.create();

    const ciphertext = CryptoJS.enc.Base64.parse(data); // Use Base64-Encoder.
    const encryptedCP = CryptoJS.lib.CipherParams.create({
      ciphertext: ciphertext,
      formatter: CryptoJS.format.OpenSSL, // Optional, but required for encryptedCP.toString()
    });
    const decryptedWA = CryptoJS.AES.decrypt(encryptedCP, key, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });

    const decryptedUtf8 = decryptedWA.toString(CryptoJS.enc.Utf8); // Avoid the Base64 detour.
    // Alternatively: CryptoJS.enc.Utf8.stringify(decryptedWA);
    return decryptedUtf8;
  }
}

export default AesService;
