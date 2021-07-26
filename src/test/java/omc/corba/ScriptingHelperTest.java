/** Copyright (C) 2016 Nicola Justus <nicola.justus@mni.thm.de>
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

package omc.corba;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static omc.corba.ScriptingHelper.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ScriptingHelperTest {

	@Test()
	public void asStringTest() {
		assertEquals(asString("test"), "\"test\"");
		assertEquals(asString(6.4), "\"6.4\"");
		assertEquals(asString(Paths.get("tmp/nico")), "\"tmp/nico\"");
	}

	@Test()
	public void asParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asParameterList(xs), "tmp, pink, cyan, eclipse");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asParameterList(numbers), "6, 5, 8, 9, 10");
	}

	@Test()
	public void asArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asArray(xs), "{tmp, pink, cyan, eclipse}");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asArray(numbers), "{6, 5, 8, 9, 10}");
	}

	@Test()
	public void asStringArrayTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asStringArray(xs), "{\"tmp\", \"pink\", \"cyan\", \"eclipse\"}");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asStringArray(numbers), "{\"6\", \"5\", \"8\", \"9\", \"10\"}");
	}

	@Test()
	public void asStringParameterListTest() {
		List<String> xs = Arrays.asList("tmp", "pink", "cyan", "eclipse");
		assertEquals(asStringParameterList(xs), "\"tmp\", \"pink\", \"cyan\", \"eclipse\"");

		List<Integer> numbers = Arrays.asList(6, 5, 8, 9, 10);
		assertEquals(asStringParameterList(numbers), "\"6\", \"5\", \"8\", \"9\", \"10\"");
	}

	@Test()
	public void killTrailingQuotesTest() {
		String bckslsh = "\\";
		String s = "\n\"eclipse is not pink! /tmp:4\"  ";
		assertEquals(killTrailingQuotes(s), "eclipse is not pink! /tmp:4");

		String s2 = "\n\"Awesome test case\"\n";
		assertEquals(killTrailingQuotes(s2), "Awesome test case");

		String s3 = "\"Check of test completed successfully.\n"+
		    "Class test has 2 equation(s) and 1 variable(s).\n"+
		    "2 of these are trivial equation(s).\"";

		assertEquals(killTrailingQuotes(s3), "Check of test completed successfully.\n"+
        "Class test has 2 equation(s) and 1 variable(s).\n"+
        "2 of these are trivial equation(s).");
		assertEquals(killTrailingQuotes("\"\""), "");
		assertEquals(killTrailingQuotes(""), "");
		assertEquals(killTrailingQuotes(" "), "");
	}

	@Test()
	public void fromArrayTest() {
		String s = "{nico, model, jenny, derb}";
		String s2 = "{nico, model, jenny, derb}";
		List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
		assertEquals(fromArray(s), exp);
		assertEquals(fromArray(s2), exp);

		String s3 = "{}";
		assertEquals(fromArray(s3), Collections.emptyList());

		String s4 = "{\"model\",\"Ideal linear electrical resistor\",false,false,false,\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\",true,53,3,113,15,false,false,\"\",\"\"}";
		List<String> exp2 = Arrays.asList(
				"\"model\"",
				"\"Ideal linear electrical resistor\"",
				"false","false","false",
				"\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\"",
				"true","53","3","113","15","false","false","\"\"","\"\"");
		assertEquals(fromArray(s4), exp2);

		String s5 = "{{{nico, \"derb\"}}}";
		assertEquals(fromArray(s5), Arrays.asList("nico", "\"derb\""));
	}

	@Test()
	public void classNameArrayTest() {
		String s = "{{Modelica.SIunits.UsersGuide,Modelica.SIunits.Icons,Modelica.SIunits.Conversions,Modelica.SIunits.Angle,Modelica.SIunits.SolidAngle}}";
		List<String> exp = Arrays.asList("Modelica.SIunits.UsersGuide","Modelica.SIunits.Icons","Modelica.SIunits.Conversions","Modelica.SIunits.Angle","Modelica.SIunits.SolidAngle");
		assertEquals(fromArray(s), exp);
	}

	@Test()
	public void largeArrayInputTest() {
		String s = "{Modelica.SIunits.UsersGuide,Modelica.SIunits.Icons,Modelica.SIunits.Conversions,Modelica.SIunits.Angle,Modelica.SIunits.SolidAngle,Modelica.SIunits.Length,Modelica.SIunits.PathLength,Modelica.SIunits.Position,Modelica.SIunits.Distance,Modelica.SIunits.Breadth,Modelica.SIunits.Height,Modelica.SIunits.Thickness,Modelica.SIunits.Radius,Modelica.SIunits.Diameter,Modelica.SIunits.Area,Modelica.SIunits.Volume,Modelica.SIunits.Time,Modelica.SIunits.Duration,Modelica.SIunits.AngularVelocity,Modelica.SIunits.AngularAcceleration,Modelica.SIunits.Velocity,Modelica.SIunits.Acceleration,Modelica.SIunits.Period,Modelica.SIunits.Frequency,Modelica.SIunits.AngularFrequency,Modelica.SIunits.Wavelength,Modelica.SIunits.Wavelenght,Modelica.SIunits.WaveNumber,Modelica.SIunits.CircularWaveNumber,Modelica.SIunits.AmplitudeLevelDifference,Modelica.SIunits.PowerLevelDifference,Modelica.SIunits.DampingCoefficient,Modelica.SIunits.LogarithmicDecrement,Modelica.SIunits.AttenuationCoefficient,Modelica.SIunits.PhaseCoefficient,Modelica.SIunits.PropagationCoefficient,Modelica.SIunits.Damping,Modelica.SIunits.Mass,Modelica.SIunits.Density,Modelica.SIunits.RelativeDensity,Modelica.SIunits.SpecificVolume,Modelica.SIunits.LinearDensity,Modelica.SIunits.SurfaceDensity,Modelica.SIunits.Momentum,Modelica.SIunits.Impulse,Modelica.SIunits.AngularMomentum,Modelica.SIunits.AngularImpulse,Modelica.SIunits.MomentOfInertia,Modelica.SIunits.Inertia,Modelica.SIunits.Force,Modelica.SIunits.TranslationalSpringConstant,Modelica.SIunits.TranslationalDampingConstant,Modelica.SIunits.Weight,Modelica.SIunits.Torque,Modelica.SIunits.ElectricalTorqueConstant,Modelica.SIunits.MomentOfForce,Modelica.SIunits.ImpulseFlowRate,Modelica.SIunits.AngularImpulseFlowRate,Modelica.SIunits.RotationalSpringConstant,Modelica.SIunits.RotationalDampingConstant,Modelica.SIunits.Pressure,Modelica.SIunits.AbsolutePressure,Modelica.SIunits.PressureDifference,Modelica.SIunits.BulkModulus,Modelica.SIunits.Stress,Modelica.SIunits.NormalStress,Modelica.SIunits.ShearStress,Modelica.SIunits.Strain,Modelica.SIunits.LinearStrain,Modelica.SIunits.ShearStrain,Modelica.SIunits.VolumeStrain,Modelica.SIunits.PoissonNumber,Modelica.SIunits.ModulusOfElasticity,Modelica.SIunits.ShearModulus,Modelica.SIunits.SecondMomentOfArea,Modelica.SIunits.SecondPolarMomentOfArea,Modelica.SIunits.SectionModulus,Modelica.SIunits.CoefficientOfFriction,Modelica.SIunits.DynamicViscosity,Modelica.SIunits.KinematicViscosity,Modelica.SIunits.SurfaceTension,Modelica.SIunits.Work,Modelica.SIunits.Energy,Modelica.SIunits.EnergyDensity,Modelica.SIunits.PotentialEnergy,Modelica.SIunits.KineticEnergy,Modelica.SIunits.Power,Modelica.SIunits.EnergyFlowRate,Modelica.SIunits.EnthalpyFlowRate,Modelica.SIunits.Efficiency,Modelica.SIunits.MassFlowRate,Modelica.SIunits.VolumeFlowRate,Modelica.SIunits.MomentumFlux,Modelica.SIunits.AngularMomentumFlux,Modelica.SIunits.ThermodynamicTemperature,Modelica.SIunits.Temp_K,Modelica.SIunits.Temperature,Modelica.SIunits.TemperatureDifference,Modelica.SIunits.Temp_C,Modelica.SIunits.TemperatureSlope,Modelica.SIunits.LinearTemperatureCoefficient,Modelica.SIunits.QuadraticTemperatureCoefficient,Modelica.SIunits.LinearExpansionCoefficient,Modelica.SIunits.CubicExpansionCoefficient,Modelica.SIunits.RelativePressureCoefficient,Modelica.SIunits.PressureCoefficient,Modelica.SIunits.Compressibility,Modelica.SIunits.IsothermalCompressibility,Modelica.SIunits.IsentropicCompressibility,Modelica.SIunits.Heat,Modelica.SIunits.HeatFlowRate,Modelica.SIunits.HeatFlux,Modelica.SIunits.DensityOfHeatFlowRate,Modelica.SIunits.ThermalConductivity,Modelica.SIunits.CoefficientOfHeatTransfer,Modelica.SIunits.SurfaceCoefficientOfHeatTransfer,Modelica.SIunits.ThermalInsulance,Modelica.SIunits.ThermalResistance,Modelica.SIunits.ThermalConductance,Modelica.SIunits.ThermalDiffusivity,Modelica.SIunits.HeatCapacity,Modelica.SIunits.SpecificHeatCapacity,Modelica.SIunits.SpecificHeatCapacityAtConstantPressure,Modelica.SIunits.SpecificHeatCapacityAtConstantVolume,Modelica.SIunits.SpecificHeatCapacityAtSaturation,Modelica.SIunits.RatioOfSpecificHeatCapacities,Modelica.SIunits.IsentropicExponent,Modelica.SIunits.Entropy,Modelica.SIunits.EntropyFlowRate,Modelica.SIunits.SpecificEntropy,Modelica.SIunits.InternalEnergy,Modelica.SIunits.Enthalpy,Modelica.SIunits.HelmholtzFreeEnergy,Modelica.SIunits.GibbsFreeEnergy,Modelica.SIunits.SpecificEnergy,Modelica.SIunits.SpecificInternalEnergy,Modelica.SIunits.SpecificEnthalpy,Modelica.SIunits.SpecificHelmholtzFreeEnergy,Modelica.SIunits.SpecificGibbsFreeEnergy,Modelica.SIunits.MassieuFunction,Modelica.SIunits.PlanckFunction,Modelica.SIunits.DerDensityByEnthalpy,Modelica.SIunits.DerDensityByPressure,Modelica.SIunits.DerDensityByTemperature,Modelica.SIunits.DerEnthalpyByPressure,Modelica.SIunits.DerEnergyByDensity,Modelica.SIunits.DerEnergyByPressure,Modelica.SIunits.DerPressureByDensity,Modelica.SIunits.DerPressureByTemperature,Modelica.SIunits.ElectricCurrent,Modelica.SIunits.Current,Modelica.SIunits.CurrentSlope,Modelica.SIunits.ElectricCharge,Modelica.SIunits.Charge,Modelica.SIunits.VolumeDensityOfCharge,Modelica.SIunits.SurfaceDensityOfCharge,Modelica.SIunits.ElectricFieldStrength,Modelica.SIunits.ElectricPotential,Modelica.SIunits.Voltage,Modelica.SIunits.PotentialDifference,Modelica.SIunits.ElectromotiveForce,Modelica.SIunits.VoltageSecond,Modelica.SIunits.VoltageSlope,Modelica.SIunits.ElectricFluxDensity,Modelica.SIunits.ElectricFlux,Modelica.SIunits.Capacitance,Modelica.SIunits.CapacitancePerArea,Modelica.SIunits.Permittivity,Modelica.SIunits.PermittivityOfVacuum,Modelica.SIunits.RelativePermittivity,Modelica.SIunits.ElectricSusceptibility,Modelica.SIunits.ElectricPolarization,Modelica.SIunits.Electrization,Modelica.SIunits.ElectricDipoleMoment,Modelica.SIunits.CurrentDensity,Modelica.SIunits.LinearCurrentDensity,Modelica.SIunits.MagneticFieldStrength,Modelica.SIunits.MagneticPotential,Modelica.SIunits.MagneticPotentialDifference,Modelica.SIunits.MagnetomotiveForce,Modelica.SIunits.CurrentLinkage,Modelica.SIunits.MagneticFluxDensity,Modelica.SIunits.MagneticFlux,Modelica.SIunits.MagneticVectorPotential,Modelica.SIunits.Inductance,Modelica.SIunits.SelfInductance,Modelica.SIunits.MutualInductance,Modelica.SIunits.CouplingCoefficient,Modelica.SIunits.LeakageCoefficient,Modelica.SIunits.Permeability,Modelica.SIunits.PermeabilityOfVacuum,Modelica.SIunits.RelativePermeability,Modelica.SIunits.MagneticSusceptibility,Modelica.SIunits.ElectromagneticMoment,Modelica.SIunits.MagneticDipoleMoment,Modelica.SIunits.Magnetization,Modelica.SIunits.MagneticPolarization,Modelica.SIunits.ElectromagneticEnergyDensity,Modelica.SIunits.PoyntingVector,Modelica.SIunits.Resistance,Modelica.SIunits.Resistivity,Modelica.SIunits.Conductivity,Modelica.SIunits.Reluctance,Modelica.SIunits.Permeance,Modelica.SIunits.PhaseDifference,Modelica.SIunits.Impedance,Modelica.SIunits.ModulusOfImpedance,Modelica.SIunits.Reactance,Modelica.SIunits.QualityFactor,Modelica.SIunits.LossAngle,Modelica.SIunits.Conductance,Modelica.SIunits.Admittance,Modelica.SIunits.ModulusOfAdmittance,Modelica.SIunits.Susceptance,Modelica.SIunits.InstantaneousPower,Modelica.SIunits.ActivePower,Modelica.SIunits.ApparentPower,Modelica.SIunits.ReactivePower,Modelica.SIunits.PowerFactor,Modelica.SIunits.Transconductance,Modelica.SIunits.InversePotential,Modelica.SIunits.ElectricalForceConstant,Modelica.SIunits.RadiantEnergy,Modelica.SIunits.RadiantEnergyDensity,Modelica.SIunits.SpectralRadiantEnergyDensity,Modelica.SIunits.RadiantPower,Modelica.SIunits.RadiantEnergyFluenceRate,Modelica.SIunits.RadiantIntensity,Modelica.SIunits.Radiance,Modelica.SIunits.RadiantExtiance,Modelica.SIunits.Irradiance,Modelica.SIunits.Emissivity,Modelica.SIunits.SpectralEmissivity,Modelica.SIunits.DirectionalSpectralEmissivity,Modelica.SIunits.LuminousIntensity,Modelica.SIunits.LuminousFlux,Modelica.SIunits.QuantityOfLight,Modelica.SIunits.Luminance,Modelica.SIunits.LuminousExitance,Modelica.SIunits.Illuminance,Modelica.SIunits.LightExposure,Modelica.SIunits.LuminousEfficacy,Modelica.SIunits.SpectralLuminousEfficacy,Modelica.SIunits.LuminousEfficiency,Modelica.SIunits.SpectralLuminousEfficiency,Modelica.SIunits.CIESpectralTristimulusValues,Modelica.SIunits.ChromaticityCoordinates,Modelica.SIunits.SpectralAbsorptionFactor,Modelica.SIunits.SpectralReflectionFactor,Modelica.SIunits.SpectralTransmissionFactor,Modelica.SIunits.SpectralRadianceFactor,Modelica.SIunits.LinearAttenuationCoefficient,Modelica.SIunits.LinearAbsorptionCoefficient,Modelica.SIunits.MolarAbsorptionCoefficient,Modelica.SIunits.RefractiveIndex,Modelica.SIunits.StaticPressure,Modelica.SIunits.SoundPressure,Modelica.SIunits.SoundParticleDisplacement,Modelica.SIunits.SoundParticleVelocity,Modelica.SIunits.SoundParticleAcceleration,Modelica.SIunits.VelocityOfSound,Modelica.SIunits.SoundEnergyDensity,Modelica.SIunits.SoundPower,Modelica.SIunits.SoundIntensity,Modelica.SIunits.AcousticImpedance,Modelica.SIunits.SpecificAcousticImpedance,Modelica.SIunits.MechanicalImpedance,Modelica.SIunits.SoundPressureLevel,Modelica.SIunits.SoundPowerLevel,Modelica.SIunits.DissipationCoefficient,Modelica.SIunits.ReflectionCoefficient,Modelica.SIunits.TransmissionCoefficient,Modelica.SIunits.AcousticAbsorptionCoefficient,Modelica.SIunits.SoundReductionIndex,Modelica.SIunits.EquivalentAbsorptionArea,Modelica.SIunits.ReverberationTime,Modelica.SIunits.LoudnessLevel,Modelica.SIunits.Loudness,Modelica.SIunits.LoundnessLevel,Modelica.SIunits.Loundness,Modelica.SIunits.RelativeAtomicMass,Modelica.SIunits.RelativeMolecularMass,Modelica.SIunits.NumberOfMolecules,Modelica.SIunits.AmountOfSubstance,Modelica.SIunits.MolarMass,Modelica.SIunits.MolarVolume,Modelica.SIunits.MolarDensity,Modelica.SIunits.MolarEnergy,Modelica.SIunits.MolarInternalEnergy,Modelica.SIunits.MolarHeatCapacity,Modelica.SIunits.MolarEntropy,Modelica.SIunits.MolarEnthalpy,Modelica.SIunits.MolarFlowRate,Modelica.SIunits.NumberDensityOfMolecules,Modelica.SIunits.MolecularConcentration,Modelica.SIunits.MassConcentration,Modelica.SIunits.MassFraction,Modelica.SIunits.Concentration,Modelica.SIunits.VolumeFraction,Modelica.SIunits.MoleFraction,Modelica.SIunits.ChemicalPotential,Modelica.SIunits.AbsoluteActivity,Modelica.SIunits.PartialPressure,Modelica.SIunits.Fugacity,Modelica.SIunits.StandardAbsoluteActivity,Modelica.SIunits.ActivityCoefficient,Modelica.SIunits.ActivityOfSolute,Modelica.SIunits.ActivityCoefficientOfSolute,Modelica.SIunits.StandardAbsoluteActivityOfSolute,Modelica.SIunits.ActivityOfSolvent,Modelica.SIunits.OsmoticCoefficientOfSolvent,Modelica.SIunits.StandardAbsoluteActivityOfSolvent,Modelica.SIunits.OsmoticPressure,Modelica.SIunits.StoichiometricNumber,Modelica.SIunits.Affinity,Modelica.SIunits.MassOfMolecule,Modelica.SIunits.ElectricDipoleMomentOfMolecule,Modelica.SIunits.ElectricPolarizabilityOfAMolecule,Modelica.SIunits.MicrocanonicalPartitionFunction,Modelica.SIunits.CanonicalPartitionFunction,Modelica.SIunits.GrandCanonicalPartitionFunction,Modelica.SIunits.MolecularPartitionFunction,Modelica.SIunits.StatisticalWeight,Modelica.SIunits.MeanFreePath,Modelica.SIunits.DiffusionCoefficient,Modelica.SIunits.ThermalDiffusionRatio,Modelica.SIunits.ThermalDiffusionFactor,Modelica.SIunits.ThermalDiffusionCoefficient,Modelica.SIunits.ElementaryCharge,Modelica.SIunits.ChargeNumberOfIon,Modelica.SIunits.FaradayConstant,Modelica.SIunits.IonicStrength,Modelica.SIunits.DegreeOfDissociation,Modelica.SIunits.ElectrolyticConductivity,Modelica.SIunits.MolarConductivity,Modelica.SIunits.TransportNumberOfIonic,Modelica.SIunits.ProtonNumber,Modelica.SIunits.NeutronNumber,Modelica.SIunits.NucleonNumber,Modelica.SIunits.AtomicMassConstant,Modelica.SIunits.MassOfElectron,Modelica.SIunits.MassOfProton,Modelica.SIunits.MassOfNeutron,Modelica.SIunits.HartreeEnergy,Modelica.SIunits.MagneticMomentOfParticle,Modelica.SIunits.BohrMagneton,Modelica.SIunits.NuclearMagneton,Modelica.SIunits.GyromagneticCoefficient,Modelica.SIunits.GFactorOfAtom,Modelica.SIunits.GFactorOfNucleus,Modelica.SIunits.LarmorAngularFrequency,Modelica.SIunits.NuclearPrecessionAngularFrequency,Modelica.SIunits.CyclotronAngularFrequency,Modelica.SIunits.NuclearQuadrupoleMoment,Modelica.SIunits.NuclearRadius,Modelica.SIunits.ElectronRadius,Modelica.SIunits.ComptonWavelength,Modelica.SIunits.MassExcess,Modelica.SIunits.MassDefect,Modelica.SIunits.RelativeMassExcess,Modelica.SIunits.RelativeMassDefect,Modelica.SIunits.PackingFraction,Modelica.SIunits.BindingFraction,Modelica.SIunits.MeanLife,Modelica.SIunits.LevelWidth,Modelica.SIunits.Activity,Modelica.SIunits.SpecificActivity,Modelica.SIunits.DecayConstant,Modelica.SIunits.HalfLife,Modelica.SIunits.AlphaDisintegrationEnergy,Modelica.SIunits.MaximumBetaParticleEnergy,Modelica.SIunits.BetaDisintegrationEnergy,Modelica.SIunits.ReactionEnergy,Modelica.SIunits.ResonanceEnergy,Modelica.SIunits.CrossSection,Modelica.SIunits.TotalCrossSection,Modelica.SIunits.AngularCrossSection,Modelica.SIunits.SpectralCrossSection,Modelica.SIunits.SpectralAngularCrossSection,Modelica.SIunits.MacroscopicCrossSection,Modelica.SIunits.TotalMacroscopicCrossSection,Modelica.SIunits.ParticleFluence,Modelica.SIunits.ParticleFluenceRate,Modelica.SIunits.EnergyFluence,Modelica.SIunits.EnergyFluenceRate,Modelica.SIunits.CurrentDensityOfParticles,Modelica.SIunits.MassAttenuationCoefficient,Modelica.SIunits.MolarAttenuationCoefficient,Modelica.SIunits.AtomicAttenuationCoefficient,Modelica.SIunits.HalfThickness,Modelica.SIunits.TotalLinearStoppingPower,Modelica.SIunits.TotalAtomicStoppingPower,Modelica.SIunits.TotalMassStoppingPower,Modelica.SIunits.MeanLinearRange,Modelica.SIunits.MeanMassRange,Modelica.SIunits.LinearIonization,Modelica.SIunits.TotalIonization,Modelica.SIunits.Mobility,Modelica.SIunits.IonNumberDensity,Modelica.SIunits.RecombinationCoefficient,Modelica.SIunits.NeutronNumberDensity,Modelica.SIunits.NeutronSpeed,Modelica.SIunits.NeutronFluenceRate,Modelica.SIunits.TotalNeutronSourceDensity,Modelica.SIunits.SlowingDownDensity,Modelica.SIunits.ResonanceEscapeProbability,Modelica.SIunits.Lethargy,Modelica.SIunits.SlowingDownArea,Modelica.SIunits.DiffusionArea,Modelica.SIunits.MigrationArea,Modelica.SIunits.SlowingDownLength,Modelica.SIunits.DiffusionLength,Modelica.SIunits.MigrationLength,Modelica.SIunits.NeutronYieldPerFission,Modelica.SIunits.NeutronYieldPerAbsorption,Modelica.SIunits.FastFissionFactor,Modelica.SIunits.ThermalUtilizationFactor,Modelica.SIunits.NonLeakageProbability,Modelica.SIunits.Reactivity,Modelica.SIunits.ReactorTimeConstant,Modelica.SIunits.EnergyImparted,Modelica.SIunits.MeanEnergyImparted,Modelica.SIunits.SpecificEnergyImparted,Modelica.SIunits.AbsorbedDose,Modelica.SIunits.DoseEquivalent,Modelica.SIunits.AbsorbedDoseRate,Modelica.SIunits.LinearEnergyTransfer,Modelica.SIunits.Kerma,Modelica.SIunits.KermaRate,Modelica.SIunits.MassEnergyTransferCoefficient,Modelica.SIunits.Exposure,Modelica.SIunits.ExposureRate,Modelica.SIunits.ReynoldsNumber,Modelica.SIunits.EulerNumber,Modelica.SIunits.FroudeNumber,Modelica.SIunits.GrashofNumber,Modelica.SIunits.WeberNumber,Modelica.SIunits.MachNumber,Modelica.SIunits.KnudsenNumber,Modelica.SIunits.StrouhalNumber,Modelica.SIunits.FourierNumber,Modelica.SIunits.PecletNumber,Modelica.SIunits.RayleighNumber,Modelica.SIunits.NusseltNumber,Modelica.SIunits.BiotNumber,Modelica.SIunits.StantonNumber,Modelica.SIunits.FourierNumberOfMassTransfer,Modelica.SIunits.PecletNumberOfMassTransfer,Modelica.SIunits.GrashofNumberOfMassTransfer,Modelica.SIunits.NusseltNumberOfMassTransfer,Modelica.SIunits.StantonNumberOfMassTransfer,Modelica.SIunits.PrandtlNumber,Modelica.SIunits.SchmidtNumber,Modelica.SIunits.LewisNumber,Modelica.SIunits.MagneticReynoldsNumber,Modelica.SIunits.AlfvenNumber,Modelica.SIunits.HartmannNumber,Modelica.SIunits.CowlingNumber,Modelica.SIunits.BraggAngle,Modelica.SIunits.OrderOfReflexion,Modelica.SIunits.ShortRangeOrderParameter,Modelica.SIunits.LongRangeOrderParameter,Modelica.SIunits.DebyeWallerFactor,Modelica.SIunits.CircularWavenumber,Modelica.SIunits.FermiCircularWavenumber,Modelica.SIunits.DebyeCircularWavenumber,Modelica.SIunits.DebyeCircularFrequency,Modelica.SIunits.DebyeTemperature,Modelica.SIunits.SpectralConcentration,Modelica.SIunits.GrueneisenParameter,Modelica.SIunits.MadelungConstant,Modelica.SIunits.DensityOfStates,Modelica.SIunits.ResidualResistivity,Modelica.SIunits.LorenzCoefficient,Modelica.SIunits.HallCoefficient,Modelica.SIunits.ThermoelectromotiveForce,Modelica.SIunits.SeebeckCoefficient,Modelica.SIunits.PeltierCoefficient,Modelica.SIunits.ThomsonCoefficient,Modelica.SIunits.RichardsonConstant,Modelica.SIunits.FermiEnergy,Modelica.SIunits.GapEnergy,Modelica.SIunits.DonorIonizationEnergy,Modelica.SIunits.AcceptorIonizationEnergy,Modelica.SIunits.ActivationEnergy,Modelica.SIunits.FermiTemperature,Modelica.SIunits.ElectronNumberDensity,Modelica.SIunits.HoleNumberDensity,Modelica.SIunits.IntrinsicNumberDensity,Modelica.SIunits.DonorNumberDensity,Modelica.SIunits.AcceptorNumberDensity,Modelica.SIunits.EffectiveMass,Modelica.SIunits.MobilityRatio,Modelica.SIunits.RelaxationTime,Modelica.SIunits.CarrierLifeTime,Modelica.SIunits.ExchangeIntegral,Modelica.SIunits.CurieTemperature,Modelica.SIunits.NeelTemperature,Modelica.SIunits.LondonPenetrationDepth,Modelica.SIunits.CoherenceLength,Modelica.SIunits.LandauGinzburgParameter,Modelica.SIunits.FluxiodQuantum,Modelica.SIunits.TimeAging,Modelica.SIunits.ChargeAging,Modelica.SIunits.PerUnit,Modelica.SIunits.DimensionlessRatio,Modelica.SIunits.ComplexCurrent,Modelica.SIunits.ComplexCurrentSlope,Modelica.SIunits.ComplexCurrentDensity,Modelica.SIunits.ComplexElectricPotential,Modelica.SIunits.ComplexPotentialDifference,Modelica.SIunits.ComplexVoltage,Modelica.SIunits.ComplexVoltageSlope,Modelica.SIunits.ComplexElectricFieldStrength,Modelica.SIunits.ComplexElectricFluxDensity,Modelica.SIunits.ComplexElectricFlux,Modelica.SIunits.ComplexMagneticFieldStrength,Modelica.SIunits.ComplexMagneticPotential,Modelica.SIunits.ComplexMagneticPotentialDifference,Modelica.SIunits.ComplexMagnetomotiveForce,Modelica.SIunits.ComplexMagneticFluxDensity,Modelica.SIunits.ComplexMagneticFlux,Modelica.SIunits.ComplexReluctance,Modelica.SIunits.ComplexImpedance,Modelica.SIunits.ComplexAdmittance,Modelica.SIunits.ComplexPower}}}}";

		assertTrue(fromArray(s).size()>5, "large modelica lists should be parseable and have a size > 5");
	}

  @Test()
  public void fromNestedArrayTest() {
    String s = "(nico, model, jenny, derb)";
    String s2 = "{nico, model, jenny, derb}";
    List<String> exp = Arrays.asList("nico", "model", "jenny", "derb");
    System.out.println(s);
    assertEquals(fromNestedArray(s), exp);
    System.out.println(s2);
    assertEquals(fromNestedArray(s2), exp);

    String s3 = "{}";
    System.out.println(s3);
    assertEquals(fromNestedArray(s3), Collections.emptyList());

    String s4 = "{\"model\",\"Ideal linear electrical resistor\",false,false,false,\"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo\",true,53,3,113,15,{},false,false,\"\",\"\"}";
    List<Object> exp2 = Arrays.asList(
				"model",
				"Ideal linear electrical resistor",
				"false", "false", "false",
				"/usr/lib/omlibrary/Modelica 3.2.2/Electrical/Analog/Basic.mo",
				"true", "53", "3", "113", "15", Collections.EMPTY_LIST, "false", "false", "", "");
		System.out.println(s4);
    assertEquals(fromNestedArrayToNestedList(s4), exp2);

    String s5 = "{{{nico, \"derb\"}}}";
    System.out.println(s5);
    assertEquals(fromNestedArray(s5), Arrays.asList("nico", "\"derb\""));
  }

	@Test()
	public void getModelNameTest() {
	  String test = "model test\nReal x = 1;\nend test;";
	  assertEquals(getModelName(test), Optional.of("test"));

   String test2 = "this is a tst file";
   assertEquals(getModelName(test2), Optional.empty());

   String test3 = "within modelica.nico.test;\nmodel Baroreceptor \"a wonderfull comment\"\nend Baroreceptor;";
   assertEquals(getModelName(test3), Optional.of("modelica.nico.test.Baroreceptor"));
	}

	@Test()
	public void getModelNameFromPathTest() throws URISyntaxException, IOException {
	  Path file1 = Paths.get(getClass().getClassLoader().getResource("ResistorTest.mo").toURI());
	  Path file2 = Paths.get(getClass().getClassLoader().getResource("ResistorTest2.mo").toURI());

	  assertEquals(getModelName(file1), Optional.of("ResistorTest"));
	  assertEquals(getModelName(file2), Optional.of("nico.components.ResistorTest"));
	}

  @Test()
  public void extractPathTest() {
    String test = "(\"package\",\"\",false,false,false,\"/Users/nico/2014-modelica-kotani/SHM/package.mo\",false,2,1,6,8,{},false,false,\"\",\"\")";
		String winTest = "this is a test,C:\\Documents\\user\\test.mo,superstring";
		assertEquals(extractPath(test), Optional.of("/Users/nico/2014-modelica-kotani/SHM/package.mo"));
    assertEquals(extractPath("/home/nico/blup.txt"), Optional.of("/home/nico/blup.txt"));
    assertEquals(extractPath("truefalse,true\"/home/nico/blup.txt\"cksiqichökajs"), Optional.of("/home/nico/blup.txt"));
    assertEquals(extractPath(winTest), Optional.of("C:\\Documents\\user\\test.mo"));
  }

	@Test()
	public void asPathTest() {
		System.setProperty("os.name", "Linux");
		assertTrue(System.getProperty("os.name") == "Linux", "`os.name` couldn't set to `Linux`");

		String path1 = "/home/user/Dokumente/Docs";
		String winPath = "C:\\Users\\chris\\Documents\\year";
		assertEquals(convertPath(path1), "\""+path1+"\"");
		assertEquals(convertPath(winPath), "\""+winPath+"\"");

		System.setProperty("os.name", "Windows");
		assertTrue(System.getProperty("os.name") == "Windows", "`os.name` couldn't set to `Windows`");

		assertEquals(convertPath(winPath), "\"C:\\\\Users\\\\chris\\\\Documents\\\\year\"");
		assertEquals(convertPath(path1), "\""+path1+"\"");
	}
}
