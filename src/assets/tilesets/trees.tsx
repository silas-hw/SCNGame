<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="trees" tilewidth="16" tileheight="32" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <image width="16" height="32" source="../sprites/testtree1.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="Wall" x="0" y="12" width="16" height="20">
    <properties>
     <property name="CollisionLayer" value="11000000"/>
     <property name="CollisionMask" value="00000000"/>
     <property name="solid" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
