package ch.epfl.tchu.game;

/**
 * @author Nikolay (314355)
 * @author Gullien (316143)
 */
public class testThingy {
    /**
     * Q2:111 Q1:010 Q0:110 Disp2:01011011 Disp1:00000110 Disp0:00111111
     * Q2:111 Q1:010 Q0:010 Disp2:01011011 Disp1:00000110 Disp0:00000110
     * Q2:111 Q1:010 Q0:111 Disp2:01011011 Disp1:00000110 Disp0:01011011
     * Q2:111 Q1:010 Q0:001 Disp2:01011011 Disp1:00000110 Disp0:01001111
     * Q2:111 Q1:010 Q0:100 Disp2:01011011 Disp1:00000110 Disp0:01100110
     * Q2:111 Q1:111 Q0:110 Disp2:01011011 Disp1:01011011 Disp0:00111111
     * Q2:111 Q1:111 Q0:010 Disp2:01011011 Disp1:01011011 Disp0:00000110
     * Q2:111 Q1:111 Q0:111 Disp2:01011011 Disp1:01011011 Disp0:01011011
     * Q2:111 Q1:111 Q0:001 Disp2:01011011 Disp1:01011011 Disp0:01001111
     * Q2:111 Q1:111 Q0:100 Disp2:01011011 Disp1:01011011 Disp0:01100110
     * Q2:111 Q1:001 Q0:110 Disp2:01011011 Disp1:01001111 Disp0:00111111
     * Q2:111 Q1:001 Q0:010 Disp2:01011011 Disp1:01001111 Disp0:00000110
     * Q2:111 Q1:001 Q0:111 Disp2:01011011 Disp1:01001111 Disp0:01011011
     * Q2:111 Q1:001 Q0:001 Disp2:01011011 Disp1:01001111 Disp0:01001111
     * Q2:111 Q1:001 Q0:100 Disp2:01011011 Disp1:01001111 Disp0:01100110
     * Q2:111 Q1:100 Q0:110 Disp2:01011011 Disp1:01100110 Disp0:00111111
     * Q2:111 Q1:100 Q0:010 Disp2:01011011 Disp1:01100110 Disp0:00000110
     * Q2:111 Q1:100 Q0:111 Disp2:01011011 Disp1:01100110 Disp0:01011011
     * Q2:111 Q1:100 Q0:001 Disp2:01011011 Disp1:01100110 Disp0:01001111
     * Q2:111 Q1:100 Q0:100 Disp2:01011011 Disp1:01100110 Disp0:01100110
     * Q2:001 Q1:110 Q0:110 Disp2:01001111 Disp1:00111111 Disp0:00111111
     * Q2:001 Q1:110 Q0:010 Disp2:01001111 Disp1:00111111 Disp0:00000110
     * Q2:001 Q1:110 Q0:111 Disp2:01001111 Disp1:00111111 Disp0:01011011
     * Q2:001 Q1:110 Q0:001 Disp2:01001111 Disp1:00111111 Disp0:01001111
     * Q2:001 Q1:110 Q0:100 Disp2:01001111 Disp1:00111111 Disp0:01100110
     * Q2:001 Q1:010 Q0:110 Disp2:01001111 Disp1:00000110 Disp0:00111111
     * Q2:001 Q1:010 Q0:010 Disp2:01001111 Disp1:00000110 Disp0:00000110
     * Q2:001 Q1:010 Q0:111 Disp2:01001111 Disp1:00000110 Disp0:01011011
     * Q2:001 Q1:010 Q0:001 Disp2:01001111 Disp1:00000110 Disp0:01001111
     * Q2:001 Q1:010 Q0:100 Disp2:01001111 Disp1:00000110 Disp0:01100110
     * Q2:001 Q1:111 Q0:110 Disp2:01001111 Disp1:01011011 Disp0:00111111
     * Q2:001 Q1:111 Q0:010 Disp2:01001111 Disp1:01011011 Disp0:00000110
     * Q2:001 Q1:111 Q0:111 Disp2:01001111 Disp1:01011011 Disp0:01011011
     * Q2:001 Q1:111 Q0:001 Disp2:01001111 Disp1:01011011 Disp0:01001111
     * Q2:001 Q1:111 Q0:100 Disp2:01001111 Disp1:01011011 Disp0:01100110
     * Q2:001 Q1:001 Q0:110 Disp2:01001111 Disp1:01001111 Disp0:00111111
     * Q2:001 Q1:001 Q0:010 Disp2:01001111 Disp1:01001111 Disp0:00000110
     * Q2:001 Q1:001 Q0:111 Disp2:01001111 Disp1:01001111 Disp0:01011011
     * Q2:001 Q1:001 Q0:001 Disp2:01001111 Disp1:01001111 Disp0:01001111
     * Q2:001 Q1:001 Q0:100 Disp2:01001111 Disp1:01001111 Disp0:01100110
     * Q2:001 Q1:100 Q0:110 Disp2:01001111 Disp1:01100110 Disp0:00111111
     * Q2:001 Q1:100 Q0:010 Disp2:01001111 Disp1:01100110 Disp0:00000110
     * Q2:001 Q1:100 Q0:111 Disp2:01001111 Disp1:01100110 Disp0:01011011
     * Q2:001 Q1:100 Q0:001 Disp2:01001111 Disp1:01100110 Disp0:01001111
     * Q2:001 Q1:100 Q0:100 Disp2:01001111 Disp1:01100110 Disp0:01100110
     * Q2:100 Q1:110 Q0:110 Disp2:01100110 Disp1:00111111 Disp0:00111111
     * Q2:100 Q1:110 Q0:010 Disp2:01100110 Disp1:00111111 Disp0:00000110
     * Q2:100 Q1:110 Q0:111 Disp2:01100110 Disp1:00111111 Disp0:01011011
     * Q2:100 Q1:110 Q0:001 Disp2:01100110 Disp1:00111111 Disp0:01001111
     * Q2:100 Q1:110 Q0:100 Disp2:01100110 Disp1:00111111 Disp0:01100110
     * Q2:100 Q1:010 Q0:110 Disp2:01100110 Disp1:00000110 Disp0:00111111
     * Q2:100 Q1:010 Q0:010 Disp2:01100110 Disp1:00000110 Disp0:00000110
     * Q2:100 Q1:010 Q0:111 Disp2:01100110 Disp1:00000110 Disp0:01011011
     * Q2:100 Q1:010 Q0:001 Disp2:01100110 Disp1:00000110 Disp0:01001111
     * Q2:100 Q1:010 Q0:100 Disp2:01100110 Disp1:00000110 Disp0:01100110
     * Q2:100 Q1:111 Q0:110 Disp2:01100110 Disp1:01011011 Disp0:00111111
     * Q2:100 Q1:111 Q0:010 Disp2:01100110 Disp1:01011011 Disp0:00000110
     * Q2:100 Q1:111 Q0:111 Disp2:01100110 Disp1:01011011 Disp0:01011011
     * Q2:100 Q1:111 Q0:001 Disp2:01100110 Disp1:01011011 Disp0:01001111
     * Q2:100 Q1:111 Q0:100 Disp2:01100110 Disp1:01011011 Disp0:01100110
     * Q2:100 Q1:001 Q0:110 Disp2:01100110 Disp1:01001111 Disp0:00111111
     * Q2:100 Q1:001 Q0:010 Disp2:01100110 Disp1:01001111 Disp0:00000110
     * Q2:100 Q1:001 Q0:111 Disp2:01100110 Disp1:01001111 Disp0:01011011
     * Q2:100 Q1:001 Q0:001 Disp2:01100110 Disp1:01001111 Disp0:01001111
     * Q2:100 Q1:001 Q0:100 Disp2:01100110 Disp1:01001111 Disp0:01100110
     * Q2:100 Q1:100 Q0:110 Disp2:01100110 Disp1:01100110 Disp0:00111111
     * Q2:100 Q1:100 Q0:010 Disp2:01100110 Disp1:01100110 Disp0:00000110
     * Q2:100 Q1:100 Q0:111 Disp2:01100110 Disp1:01100110 Disp0:01011011
     * Q2:100 Q1:100 Q0:001 Disp2:01100110 Disp1:01100110 Disp0:01001111
     * Q2:100 Q1:100 Q0:100 Disp2:01100110 Disp1:01100110 Disp0:01100110
     * Q2:110 Q1:110 Q0:110 Disp2:00111111 Disp1:00111111 Disp0:00111111
     * Q2:110 Q1:110 Q0:010 Disp2:00111111 Disp1:00111111 Disp0:00000110
     * Q2:110 Q1:110 Q0:111 Disp2:00111111 Disp1:00111111 Disp0:01011011
     * Q2:110 Q1:110 Q0:001 Disp2:00111111 Disp1:00111111 Disp0:01001111
     * Q2:110 Q1:110 Q0:100 Disp2:00111111 Disp1:00111111 Disp0:01100110
     * Q2:110 Q1:010 Q0:110 Disp2:00111111 Disp1:00000110 Disp0:00111111
     * Q2:110 Q1:010 Q0:010 Disp2:00111111 Disp1:00000110 Disp0:00000110
     * Q2:110 Q1:010 Q0:111 Disp2:00111111 Disp1:00000110 Disp0:01011011
     * Q2:110 Q1:010 Q0:001 Disp2:00111111 Disp1:00000110 Disp0:01001111
     * Q2:110 Q1:010 Q0:100 Disp2:00111111 Disp1:00000110 Disp0:01100110
     * Q2:110 Q1:111 Q0:110 Disp2:00111111 Disp1:01011011 Disp0:00111111
     * Q2:110 Q1:111 Q0:010 Disp2:00111111 Disp1:01011011 Disp0:00000110
     * Q2:110 Q1:111 Q0:111 Disp2:00111111 Disp1:01011011 Disp0:01011011
     * Q2:110 Q1:111 Q0:001 Disp2:00111111 Disp1:01011011 Disp0:01001111
     * Q2:110 Q1:111 Q0:100 Disp2:00111111 Disp1:01011011 Disp0:01100110
     * Q2:110 Q1:001 Q0:110 Disp2:00111111 Disp1:01001111 Disp0:00111111
     * Q2:110 Q1:001 Q0:010 Disp2:00111111 Disp1:01001111 Disp0:00000110
     * Q2:110 Q1:001 Q0:111 Disp2:00111111 Disp1:01001111 Disp0:01011011
     * Q2:110 Q1:001 Q0:001 Disp2:00111111 Disp1:01001111 Disp0:01001111
     * Q2:110 Q1:001 Q0:100 Disp2:00111111 Disp1:01001111 Disp0:01100110
     * Q2:110 Q1:100 Q0:110 Disp2:00111111 Disp1:01100110 Disp0:00111111
     * Q2:110 Q1:100 Q0:010 Disp2:00111111 Disp1:01100110 Disp0:00000110
     * Q2:110 Q1:100 Q0:111 Disp2:00111111 Disp1:01100110 Disp0:01011011
     * Q2:110 Q1:100 Q0:001 Disp2:00111111 Disp1:01100110 Disp0:01001111
     * Q2:110 Q1:100 Q0:100 Disp2:00111111 Disp1:01100110 Disp0:01100110
     * Q2:010 Q1:110 Q0:110 Disp2:00000110 Disp1:00111111 Disp0:00111111
     * Q2:010 Q1:110 Q0:010 Disp2:00000110 Disp1:00111111 Disp0:00000110
     * Q2:010 Q1:110 Q0:111 Disp2:00000110 Disp1:00111111 Disp0:01011011
     * Q2:010 Q1:110 Q0:001 Disp2:00000110 Disp1:00111111 Disp0:01001111
     * Q2:010 Q1:110 Q0:100 Disp2:00000110 Disp1:00111111 Disp0:01100110
     * Q2:010 Q1:010 Q0:110 Disp2:00000110 Disp1:00000110 Disp0:00111111
     * Q2:010 Q1:010 Q0:010 Disp2:00000110 Disp1:00000110 Disp0:00000110
     * Q2:010 Q1:010 Q0:111 Disp2:00000110 Disp1:00000110 Disp0:01011011
     * Q2:010 Q1:010 Q0:001 Disp2:00000110 Disp1:00000110 Disp0:01001111
     * Q2:010 Q1:010 Q0:100 Disp2:00000110 Disp1:00000110 Disp0:01100110
     * Q2:010 Q1:111 Q0:110 Disp2:00000110 Disp1:01011011 Disp0:00111111
     * Q2:010 Q1:111 Q0:010 Disp2:00000110 Disp1:01011011 Disp0:00000110
     * Q2:010 Q1:111 Q0:111 Disp2:00000110 Disp1:01011011 Disp0:01011011
     * Q2:010 Q1:111 Q0:001 Disp2:00000110 Disp1:01011011 Disp0:01001111
     * Q2:010 Q1:111 Q0:100 Disp2:00000110 Disp1:01011011 Disp0:01100110
     * Q2:010 Q1:001 Q0:110 Disp2:00000110 Disp1:01001111 Disp0:00111111
     * Q2:010 Q1:001 Q0:010 Disp2:00000110 Disp1:01001111 Disp0:00000110
     * Q2:010 Q1:001 Q0:111 Disp2:00000110 Disp1:01001111 Disp0:01011011
     * Q2:010 Q1:001 Q0:001 Disp2:00000110 Disp1:01001111 Disp0:01001111
     * Q2:010 Q1:001 Q0:100 Disp2:00000110 Disp1:01001111 Disp0:01100110
     * Q2:010 Q1:100 Q0:110 Disp2:00000110 Disp1:01100110 Disp0:00111111
     * Q2:010 Q1:100 Q0:010 Disp2:00000110 Disp1:01100110 Disp0:00000110
     * Q2:010 Q1:100 Q0:111 Disp2:00000110 Disp1:01100110 Disp0:01011011
     * Q2:010 Q1:100 Q0:001 Disp2:00000110 Disp1:01100110 Disp0:01001111
     * Q2:010 Q1:100 Q0:100 Disp2:00000110 Disp1:01100110 Disp0:01100110
     * Q2:111 Q1:110 Q0:110 Disp2:01011011 Disp1:00111111 Disp0:00111111
     * Q2:111 Q1:110 Q0:010 Disp2:01011011 Disp1:00111111 Disp0:00000110
     * Q2:111 Q1:110 Q0:111 Disp2:01011011 Disp1:00111111 Disp0:01011011
     * Q2:111 Q1:110 Q0:001 Disp2:01011011 Disp1:00111111 Disp0:01001111
     * Q2:111 Q1:110 Q0:100 Disp2:01011011 Disp1:00111111 Disp0:01100110
     * Q2:111 Q1:010 Q0:110 Disp2:01011011 Disp1:00000110 Disp0:00111111
     * Q2:111 Q1:010 Q0:010 Disp2:01011011 Disp1:00000110 Disp0:00000110
     * Q2:111 Q1:010 Q0:110 Disp2:01011011 Disp1:00000110 Disp0:00111111
     * Q2:111 Q1:110 Q0:100 Disp2:01011011 Disp1:00111111 Disp0:01100110
     * Q2:111 Q1:110 Q0:001 Disp2:01011011 Disp1:00111111 Disp0:01001111
     * Q2:111 Q1:110 Q0:111 Disp2:01011011 Disp1:00111111 Disp0:01011011
     * Q2:111 Q1:110 Q0:010 Disp2:01011011 Disp1:00111111 Disp0:00000110
     * Q2:111 Q1:110 Q0:110 Disp2:01011011 Disp1:00111111 Disp0:00111111
     * Q2:010 Q1:100 Q0:100 Disp2:00000110 Disp1:01100110 Disp0:01100110
     * Q2:010 Q1:100 Q0:001 Disp2:00000110 Disp1:01100110 Disp0:01001111
     * Q2:010 Q1:100 Q0:111 Disp2:00000110 Disp1:01100110 Disp0:01011011
     * Q2:010 Q1:100 Q0:010 Disp2:00000110 Disp1:01100110 Disp0:00000110
     * Q2:010 Q1:100 Q0:110 Disp2:00000110 Disp1:01100110 Disp0:00111111
     * Q2:010 Q1:001 Q0:100 Disp2:00000110 Disp1:01001111 Disp0:01100110
     * Q2:010 Q1:001 Q0:001 Disp2:00000110 Disp1:01001111 Disp0:01001111
     * Q2:010 Q1:001 Q0:111 Disp2:00000110 Disp1:01001111 Disp0:01011011
     * Q2:010 Q1:001 Q0:010 Disp2:00000110 Disp1:01001111 Disp0:00000110
     * Q2:010 Q1:001 Q0:110 Disp2:00000110 Disp1:01001111 Disp0:00111111
     * Q2:010 Q1:111 Q0:100 Disp2:00000110 Disp1:01011011 Disp0:01100110
     * Q2:010 Q1:111 Q0:001 Disp2:00000110 Disp1:01011011 Disp0:01001111
     * Q2:010 Q1:111 Q0:111 Disp2:00000110 Disp1:01011011 Disp0:01011011
     * Q2:010 Q1:111 Q0:010 Disp2:00000110 Disp1:01011011 Disp0:00000110
     * Q2:010 Q1:111 Q0:110 Disp2:00000110 Disp1:01011011 Disp0:00111111
     * Q2:010 Q1:010 Q0:100 Disp2:00000110 Disp1:00000110 Disp0:01100110
     * Q2:010 Q1:010 Q0:001 Disp2:00000110 Disp1:00000110 Disp0:01001111
     * Q2:010 Q1:010 Q0:111 Disp2:00000110 Disp1:00000110 Disp0:01011011
     * Q2:010 Q1:010 Q0:010 Disp2:00000110 Disp1:00000110 Disp0:00000110
     * Q2:010 Q1:010 Q0:110 Disp2:00000110 Disp1:00000110 Disp0:00111111
     * Q2:010 Q1:110 Q0:100 Disp2:00000110 Disp1:00111111 Disp0:01100110
     * Q2:010 Q1:110 Q0:001 Disp2:00000110 Disp1:00111111 Disp0:01001111
     * Q2:010 Q1:110 Q0:111 Disp2:00000110 Disp1:00111111 Disp0:01011011
     * Q2:010 Q1:110 Q0:010 Disp2:00000110 Disp1:00111111 Disp0:00000110
     * Q2:010 Q1:110 Q0:110 Disp2:00000110 Disp1:00111111 Disp0:00111111
     * Q2:110 Q1:100 Q0:100 Disp2:00111111 Disp1:01100110 Disp0:01100110
     * Q2:110 Q1:100 Q0:001 Disp2:00111111 Disp1:01100110 Disp0:01001111
     * Q2:110 Q1:100 Q0:111 Disp2:00111111 Disp1:01100110 Disp0:01011011
     * Q2:110 Q1:100 Q0:010 Disp2:00111111 Disp1:01100110 Disp0:00000110
     * Q2:110 Q1:100 Q0:110 Disp2:00111111 Disp1:01100110 Disp0:00111111
     * Q2:110 Q1:001 Q0:100 Disp2:00111111 Disp1:01001111 Disp0:01100110
     * Q2:110 Q1:001 Q0:001 Disp2:00111111 Disp1:01001111 Disp0:01001111
     * Q2:110 Q1:001 Q0:111 Disp2:00111111 Disp1:01001111 Disp0:01011011
     * Q2:110 Q1:001 Q0:010 Disp2:00111111 Disp1:01001111 Disp0:00000110
     * Q2:110 Q1:001 Q0:110 Disp2:00111111 Disp1:01001111 Disp0:00111111
     * Q2:110 Q1:111 Q0:100 Disp2:00111111 Disp1:01011011 Disp0:01100110
     * Q2:110 Q1:111 Q0:001 Disp2:00111111 Disp1:01011011 Disp0:01001111
     * Q2:110 Q1:111 Q0:111 Disp2:00111111 Disp1:01011011 Disp0:01011011
     * Q2:110 Q1:111 Q0:010 Disp2:00111111 Disp1:01011011 Disp0:00000110
     * Q2:110 Q1:111 Q0:110 Disp2:00111111 Disp1:01011011 Disp0:00111111
     * Q2:110 Q1:010 Q0:100 Disp2:00111111 Disp1:00000110 Disp0:01100110
     * Q2:110 Q1:010 Q0:001 Disp2:00111111 Disp1:00000110 Disp0:01001111
     * Q2:110 Q1:010 Q0:111 Disp2:00111111 Disp1:00000110 Disp0:01011011
     * Q2:110 Q1:010 Q0:010 Disp2:00111111 Disp1:00000110 Disp0:00000110
     * Q2:110 Q1:010 Q0:110 Disp2:00111111 Disp1:00000110 Disp0:00111111
     * Q2:110 Q1:110 Q0:100 Disp2:00111111 Disp1:00111111 Disp0:01100110
     * Q2:110 Q1:110 Q0:001 Disp2:00111111 Disp1:00111111 Disp0:01001111
     * Q2:110 Q1:110 Q0:111 Disp2:00111111 Disp1:00111111 Disp0:01011011
     * Q2:110 Q1:110 Q0:010 Disp2:00111111 Disp1:00111111 Disp0:00000110
     * Q2:110 Q1:110 Q0:110 Disp2:00111111 Disp1:00111111 Disp0:00111111
     * Q2:100 Q1:100 Q0:100 Disp2:01100110 Disp1:01100110 Disp0:01100110
     * Q2:100 Q1:100 Q0:001 Disp2:01100110 Disp1:01100110 Disp0:01001111
     * Q2:100 Q1:100 Q0:111 Disp2:01100110 Disp1:01100110 Disp0:01011011
     * Q2:100 Q1:100 Q0:010 Disp2:01100110 Disp1:01100110 Disp0:00000110
     * Q2:100 Q1:100 Q0:110 Disp2:01100110 Disp1:01100110 Disp0:00111111
     * Q2:100 Q1:001 Q0:100 Disp2:01100110 Disp1:01001111 Disp0:01100110
     * Q2:100 Q1:001 Q0:001 Disp2:01100110 Disp1:01001111 Disp0:01001111
     * Q2:100 Q1:001 Q0:111 Disp2:01100110 Disp1:01001111 Disp0:01011011
     * Q2:100 Q1:001 Q0:010 Disp2:01100110 Disp1:01001111 Disp0:00000110
     * Q2:100 Q1:001 Q0:110 Disp2:01100110 Disp1:01001111 Disp0:00111111
     * Q2:100 Q1:111 Q0:100 Disp2:01100110 Disp1:01011011 Disp0:01100110
     * Q2:100 Q1:111 Q0:001 Disp2:01100110 Disp1:01011011 Disp0:01001111
     * Q2:100 Q1:111 Q0:111 Disp2:01100110 Disp1:01011011 Disp0:01011011
     * Q2:100 Q1:111 Q0:010 Disp2:01100110 Disp1:01011011 Disp0:00000110
     * Q2:100 Q1:111 Q0:110 Disp2:01100110 Disp1:01011011 Disp0:00111111
     * Q2:100 Q1:010 Q0:100 Disp2:01100110 Disp1:00000110 Disp0:01100110
     * Q2:100 Q1:010 Q0:001 Disp2:01100110 Disp1:00000110 Disp0:01001111
     * Q2:100 Q1:010 Q0:111 Disp2:01100110 Disp1:00000110 Disp0:01011011
     * Q2:100 Q1:010 Q0:010 Disp2:01100110 Disp1:00000110 Disp0:00000110
     * Q2:100 Q1:010 Q0:110 Disp2:01100110 Disp1:00000110 Disp0:00111111
     * Q2:100 Q1:110 Q0:100 Disp2:01100110 Disp1:00111111 Disp0:01100110
     * Q2:100 Q1:110 Q0:001 Disp2:01100110 Disp1:00111111 Disp0:01001111
     * Q2:100 Q1:110 Q0:111 Disp2:01100110 Disp1:00111111 Disp0:01011011
     * Q2:100 Q1:110 Q0:010 Disp2:01100110 Disp1:00111111 Disp0:00000110
     * Q2:100 Q1:110 Q0:110 Disp2:01100110 Disp1:00111111 Disp0:00111111
     * Q2:001 Q1:100 Q0:100 Disp2:01001111 Disp1:01100110 Disp0:01100110
     * Q2:001 Q1:100 Q0:001 Disp2:01001111 Disp1:01100110 Disp0:01001111
     * Q2:001 Q1:100 Q0:111 Disp2:01001111 Disp1:01100110 Disp0:01011011
     * Q2:001 Q1:100 Q0:010 Disp2:01001111 Disp1:01100110 Disp0:00000110
     * Q2:001 Q1:100 Q0:110 Disp2:01001111 Disp1:01100110 Disp0:00111111
     * Q2:001 Q1:001 Q0:100 Disp2:01001111 Disp1:01001111 Disp0:01100110
     * Q2:001 Q1:001 Q0:001 Disp2:01001111 Disp1:01001111 Disp0:01001111
     * Q2:001 Q1:001 Q0:111 Disp2:01001111 Disp1:01001111 Disp0:01011011
     * Q2:001 Q1:001 Q0:010 Disp2:01001111 Disp1:01001111 Disp0:00000110
     * Q2:001 Q1:001 Q0:110 Disp2:01001111 Disp1:01001111 Disp0:00111111
     * Q2:001 Q1:111 Q0:100 Disp2:01001111 Disp1:01011011 Disp0:01100110
     * Q2:001 Q1:111 Q0:001 Disp2:01001111 Disp1:01011011 Disp0:01001111
     * Q2:001 Q1:111 Q0:111 Disp2:01001111 Disp1:01011011 Disp0:01011011
     * Q2:001 Q1:111 Q0:010 Disp2:01001111 Disp1:01011011 Disp0:00000110
     * Q2:001 Q1:111 Q0:110 Disp2:01001111 Disp1:01011011 Disp0:00111111
     * Q2:001 Q1:010 Q0:100 Disp2:01001111 Disp1:00000110 Disp0:01100110
     * Q2:001 Q1:010 Q0:001 Disp2:01001111 Disp1:00000110 Disp0:01001111
     * Q2:001 Q1:010 Q0:111 Disp2:01001111 Disp1:00000110 Disp0:01011011
     * Q2:001 Q1:010 Q0:010 Disp2:01001111 Disp1:00000110 Disp0:00000110
     * Q2:001 Q1:010 Q0:110 Disp2:01001111 Disp1:00000110 Disp0:00111111
     * Q2:001 Q1:110 Q0:100 Disp2:01001111 Disp1:00111111 Disp0:01100110
     * Q2:001 Q1:110 Q0:001 Disp2:01001111 Disp1:00111111 Disp0:01001111
     * Q2:001 Q1:110 Q0:111 Disp2:01001111 Disp1:00111111 Disp0:01011011
     * Q2:001 Q1:110 Q0:010 Disp2:01001111 Disp1:00111111 Disp0:00000110
     * Q2:001 Q1:110 Q0:110 Disp2:01001111 Disp1:00111111 Disp0:00111111
     * Q2:111 Q1:100 Q0:100 Disp2:01011011 Disp1:01100110 Disp0:01100110
     * Q2:111 Q1:100 Q0:001 Disp2:01011011 Disp1:01100110 Disp0:01001111
     * Q2:111 Q1:100 Q0:111 Disp2:01011011 Disp1:01100110 Disp0:01011011
     * Q2:111 Q1:100 Q0:010 Disp2:01011011 Disp1:01100110 Disp0:00000110
     * Q2:111 Q1:100 Q0:110 Disp2:01011011 Disp1:01100110 Disp0:00111111
     * Q2:111 Q1:001 Q0:100 Disp2:01011011 Disp1:01001111 Disp0:01100110
     * Q2:111 Q1:001 Q0:001 Disp2:01011011 Disp1:01001111 Disp0:01001111
     * Q2:111 Q1:001 Q0:111 Disp2:01011011 Disp1:01001111 Disp0:01011011
     * Q2:111 Q1:001 Q0:010 Disp2:01011011 Disp1:01001111 Disp0:00000110
     * Q2:111 Q1:001 Q0:110 Disp2:01011011 Disp1:01001111 Disp0:00111111
     * Q2:111 Q1:111 Q0:100 Disp2:01011011 Disp1:01011011 Disp0:01100110
     * Q2:111 Q1:111 Q0:001 Disp2:01011011 Disp1:01011011 Disp0:01001111
     * Q2:111 Q1:111 Q0:111 Disp2:01011011 Disp1:01011011 Disp0:01011011
     * Q2:111 Q1:111 Q0:010 Disp2:01011011 Disp1:01011011 Disp0:00000110
     * Q2:111 Q1:111 Q0:110 Disp2:01011011 Disp1:01011011 Disp0:00111111
     * Q2:111 Q1:010 Q0:100 Disp2:01011011 Disp1:00000110 Disp0:01100110
     * Q2:111 Q1:010 Q0:001 Disp2:01011011 Disp1:00000110 Disp0:01001111
     * Q2:111 Q1:010 Q0:111 Disp2:01011011 Disp1:00000110 Disp0:01011011
     * Q2:111 Q1:010 Q0:010 Disp2:01011011 Disp1:00000110 Disp0:00000110
     * Q2:111 Q1:010 Q0:110 Disp2:01011011 Disp1:00000110 Disp0:00111111
     * Q2:111 Q1:110 Q0:100 Disp2:01011011 Disp1:00111111 Disp0:01100110
     */
}
