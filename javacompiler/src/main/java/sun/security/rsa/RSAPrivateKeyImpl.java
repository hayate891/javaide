/*
 * Copyright (c) 2003, 2008, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package sun.security.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;

import sun.security.pkcs.PKCS8Key;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/**
 * Key implementation for RSA private keys, non-CRT form (modulus, private
 * exponent only). For CRT private keys, see RSAPrivateCrtKeyImpl. We need
 * separate classes to ensure correct behavior in instanceof checks, etc.
 * <p>
 * Note: RSA keys must be at least 512 bits long
 *
 * @author Andreas Sterbenz
 * @see RSAPrivateCrtKeyImpl
 * @see RSAKeyFactory
 * @since 1.5
 */
public final class RSAPrivateKeyImpl extends PKCS8Key implements RSAPrivateKey {

    private static final long serialVersionUID = -33106691987952810L;

    private final BigInteger n;         // modulus
    private final BigInteger d;         // private exponent

    /**
     * Construct a key from its components. Used by the
     * RSAKeyFactory and the RSAKeyPairGenerator.
     */
    RSAPrivateKeyImpl(BigInteger n, BigInteger d) throws InvalidKeyException {
        this.n = n;
        this.d = d;
        RSAKeyFactory.checkRSAProviderKeyLengths(n.bitLength(), null);
        // generate the encoding
        algid = RSAPrivateCrtKeyImpl.rsaId;
        try {
            DerOutputStream out = new DerOutputStream();
            out.putInteger(0); // version must be 0
            out.putInteger(n);
            out.putInteger(0);
            out.putInteger(d);
            out.putInteger(0);
            out.putInteger(0);
            out.putInteger(0);
            out.putInteger(0);
            out.putInteger(0);
            DerValue val =
                    new DerValue(DerValue.tag_Sequence, out.toByteArray());
            key = val.toByteArray();
        } catch (IOException exc) {
            // should never occur
            throw new InvalidKeyException(exc);
        }
    }

    // see JCA doc
    public String getAlgorithm() {
        return "RSA";
    }

    // see JCA doc
    public BigInteger getModulus() {
        return n;
    }

    // see JCA doc
    public BigInteger getPrivateExponent() {
        return d;
    }

    // return a string representation of this key for debugging
    public String toString() {
        return "Sun RSA private key, " + n.bitLength() + " bits\n  modulus: "
                + n + "\n  private exponent: " + d;
    }

}
