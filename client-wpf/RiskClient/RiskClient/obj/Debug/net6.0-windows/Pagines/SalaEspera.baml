   M S B A M L   `   `   `  ����E  ARiskClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullA%clr-namespace:RiskClient.UserControlsRiskClient.UserControls  5      #    RiskClient.Pagines.SalaEspera   5      W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5      R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      4uc%clr-namespace:RiskClient.UserControls     5        N� Title$  
SalaEspera��5	      .��5      � ��5   
   �� 5       �� ImageSource$E ?E:\M13 - PROJECTE\RiskClient\RiskClient\Imatges\sala_espera.png��6    j� Stretch$ UniformToFill=�6g   .+�5   
   �� $��80,40,80,40q�6   ���	#AAFFFFFF �� CornerRadius$ 16��6=   ���#DDD$��1q�6b    �� Padding$ 20q�6v   ._�5      �  � RowDefinitions 5      �� 5      $
��Auto �6$   �� 5      $��* �6$   �� 5      $
��Auto �6$   .+�5      �� $��ESPERANT JUGADORS...��6   $��0��68   $��Center=�6E   $	��Top=�6b   $��0,0,0,20q�6z   $��40(�5      $
��Bold"�6*   ���#004797$��Comic Sans MS)�6Q   $
��Wrap=�6l   �� 5       -   5         ��Name$ 	lvPlayers��5       $��1��6.   
������ $��0q�6t   $��Center=�5!      $	��Top=�68   $	��950��6P   $	��680��6\   $��Disabled=�5"      $��Disabled=�6O   ��5    ;   �� *	Usuaris+��5#      �� 5$      .R�5%      A�  A� Columns$ 2��6*    A� Rows$ 3��66   $��10q�6?   5&      ��5(      �� 5)      	 �� 
TargetType"	 ��6    .�5*      �� 
 �� Property
 � �� �� Value$ 10q�67   �� 5+      
 � ��$ Center=�6K   �� 5,      
 � ��$ Center=�6I   5-      ��5/      �� 50      .R�51      '   !RiskClient.UserControls.UCJugador     usuaris 6+   �� 52      54      �� 57      $��2��6    �� Orientation$ 
Horizontal=�6*   $��Center=�6C   $��Center=�6`   $��0,20,0,0q�6{   .+�58      �� -   5         ��Name$ 
btnIniciar��58      $��INICIAR��61   $	��220��6C   $��60��6O   $��24(�6[   ���#004797
�������$��0q�59      $��10q�61    � Cursor$
 Hand��6>   $��0,0,20,0q�6L   ��5:      �� 5;       �� 
TargetType" ��6.   .R�5<   "   �� #��z��6)   $ 30��6S   # z��6e   ._�5=   &   �� $��Center=�67   $��Center=�6T   $
��True��6o   5>   #   5?      5A      �� 5C      -   5        $ btnCancelar��5C      $��Cancel·lar��62   $	��220��6G   $��60��6S   $��24(�6_   
�������
���   �$��0q�5D      $��10q�61   $
 Hand��6>   ��5E      �� 5F      " ��6.   .R�5G   "   �� #��z��6)   $ 30��6S   # z��6e   ._�5H   &   �� $��Center=�67   $��Center=�6T   $
��True��6o   5I   #   5J      5L      5M      5N      5O      5P      5Q      