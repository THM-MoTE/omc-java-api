model ResistorTest
  Modelica.Electrical.Analog.Basic.Ground ground1 annotation(Placement(visible = true, transformation(origin = {-56, 14}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
  Modelica.Electrical.Analog.Basic.Resistor resistor1 annotation(Placement(visible = true, transformation(origin = {-12, 56}, extent = {{-10, -10}, {10, 10}}, rotation = 0)));
  Modelica.Electrical.Analog.Basic.Resistor resistor2 annotation(Placement(visible = true, transformation(origin = {28, 34}, extent = {{-10, -10}, {10, 10}}, rotation = -90)));
//  fibs f;
//  test2 t;
equation
  connect(resistor1.R, ground1.p) annotation(Line(points = {{28, 24}, {-56, 24}}, color = {0, 0, 255}));
  connect(resistor1.n, resistor2.p) annotation(Line(points = {{-2, 56}, {28, 56}, {28, 44}}, color = {0, 0, 255}));
  connect(ground1.p, resistor1.p) annotation(Line(points = {{-56, 24}, {-22, 24}, {-22, 56}}, color = {0, 0, 255}));
  annotation(uses(Modelica(version = "3.2.1")));
  x = 0.5;
end ResistorTest;
