   M S B A M L   `   `   `  ����E  ARiskClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null5clr-namespace:RiskClient.ModelsRiskClient.Models  5      +    %RiskClient.Models.IniciSessioRegistre   5      W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5      R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      1localclr-namespace:RiskClient.Models     5        N� Title$  IniciSessioRegistre��5
      .��5      � .+�5   
   �� $H��BE:\M13 - PROJECTE\RiskClient\RiskClient\Imatges\imatge_portada.png��6   $
��Fill=�5      $��Stretch=�6   $��Stretch=�6:   � 5   
   $��Bottom=�6   $��Center=�6*   $��20,20,20,100q�6G   .+�5      �� ���	#CC1A2233 �� CornerRadius$ 20��6,    �� Padding$ 32q�6>    J� Effect 6K   Z� ._�5      �� $	��300��6   .+�5      �� -   5         ��Name$ 
LoginPanel��5   !   .+�5      �� $��INICI DE SESSIÓ��6$   $��24(�6;   $
��Bold"�6I   ���	#FFF6E7C1$��Center=�6r   $��0,0,0,20q�6�   �� 5      .+�5      �� $��Usuari��6(   ���	#FFF6E7C1$��14(�6M   $��0,0,0,4q�6[   �� 5      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5   "   �� -   5         ��Name$ 	TxtUsuari��5   *   
������ $��0q�6V   $
��10,4q�6j   ���	#FFF6E7C1$��14(�6�   5      5      �� 5"      $��0,10,0,0q�6%   .+�5#      �� $��Contrasenya��6(   ���	#FFF6E7C1$��14(�6R   $��0,0,0,4q�6`   �� 5$      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5%   "   G� -   5         G�Name$ TxtContrasenya��5%   .   
������ $��0q�6_   $
��10,4q�6s   ���	#FFF6E7C1$��14(�6�   5&      5'      �� 5*      -   5         ��Name$ 	BtnInicia��5*   !   $
��Auto��64   $��45��6A   $��INICIA��6M   $��Stretch=�6^   ���	#FFED7D31
�������$
��Bold"�5+   C   $��16(�6U   $��0q�6c   $��0,20,0,0q�6w   6�   �� 5.      -   5        $ ��No tens compte? Registra't��5.   $   ���	#FFED7D31$��12(�6]   $��Center=�6k   $��0,10,0,0q�6�    � Cursor$
 Hand��6�   5/      50      �� 53      -   5        $ RegistrePanel��53   !   $y�	Collapsed=�68   .+�54      �� $��
REGISTRA'T��6$   $��24(�66   $
��Bold"�6D   ���	#FFF6E7C1$��Center=�6m   $��0,0,0,20q�6�   �� 57      .+�58      �� $	��Nom��6(   ���	#FFF6E7C1$��14(�6J   $��0,0,0,4q�6X   �� 59      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5:   "   �� -   5        $ TxtNom��5:   *   
������ $��0q�6S   $
��10,4q�6g   ���	#FFF6E7C1$��14(�6�   5;      5<      �� 5?      $��0,10,0,0q�6%   .+�5@      �� $��Nom d'usuari (Nickname)��6(   ���	#FFF6E7C1$��14(�6^   $��0,0,0,4q�6l   �� 5A      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5B   "   �� -   5        $ TxtNickname��5B   *   
������ $��0q�6X   $
��10,4q�6l   ���	#FFF6E7C1$��14(�6�   5C      5D      �� 5G      $��0,10,0,0q�6%   .+�5H      �� $��Contrasenya��6(   ���	#FFF6E7C1$��14(�6R   $��0,0,0,4q�6`   �� 5I      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5J   "   G� -	   5        $ TxtPasswordRegistre��5J   .   
������ $��0q�6d   $
��10,4q�6x   ���	#FFF6E7C1$��14(�6�   5K      5L      �� 5O      $��0,10,0,0q�6%   .+�5P      �� $��Confirma la contrasenya��6(   ���	#FFF6E7C1$��14(�6^   $��0,0,0,4q�6l   �� 5Q      $ 10��6%   ���	#FFED7D31$	��1.5q�6O   ���	#33FFFFFF._�5R   "   G� -
   5        $ TxtConfirmaPassword��5R   .   
������ $��0q�6d   $
��10,4q�6x   ���	#FFF6E7C1$��14(�6�   5S      5T      �� 5W      -   5        $ BtnRegistrar��5W   !   $
��Auto��67   $��45��6D   $��	REGISTRAR��6P   $��Stretch=�6d   ���	#FFED7D31
�������$
��Bold"�5X   C   $��16(�6U   $��0q�6c   $��0,20,0,0q�6w   6�   �� 5[      -   5        $$��Ja tens compte? Inicia sessió��5[   $   ���	#FFED7D31$��12(�6`   $��Center=�6n   $��0,10,0,0q�6�   $
 Hand��6�   5\      5]      5_      5`      5a      5b      5c      